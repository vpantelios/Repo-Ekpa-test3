terraform {
  required_version = ">= 1.5.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.region
}

# Ubuntu 24.04 LTS (Canonical) – φέρνουμε ΠΑΝΤΑ το πιο πρόσφατο AMI της περιοχής
data "aws_ami" "ubuntu_24" {
  owners      = ["099720109477"] # Canonical
  most_recent = true
  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-noble-24.04-amd64-server-*"]
  }
  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

############################
# 1) DB instance -> DB AMI #
############################
resource "aws_instance" "db" {
  ami                    = data.aws_ami.ubuntu_24.id
  instance_type          = var.instance_type_db
  key_name               = var.key_name
  associate_public_ip_address = true

  tags = { Name = "build-db-image" }

  # Προvisioning μέσω SSH (remote-exec), όπως στο βιβλίο. :contentReference[oaicite:2]{index=2}
  provisioner "remote-exec" {
    inline = [
      "sudo apt-get update -y",
      "sudo DEBIAN_FRONTEND=noninteractive apt-get install -y mysql-server",
      # επιτρέπουμε remote access + φτιάχνουμε DB/χρήστη
      "sudo sed -i 's/^bind-address.*/bind-address = 0.0.0.0/' /etc/mysql/mysql.conf.d/mysqld.cnf || true",
      "sudo systemctl enable mysql && sudo systemctl restart mysql",
      "mysql -uroot -e \"CREATE DATABASE IF NOT EXISTS ${var.db_name};\"",
      "mysql -uroot -e \"CREATE USER IF NOT EXISTS '${var.db_user}'@'%' IDENTIFIED BY '${var.db_password}';\"",
      "mysql -uroot -e \"GRANT ALL PRIVILEGES ON ${var.db_name}.* TO '${var.db_user}'@'%'; FLUSH PRIVILEGES;\""
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file(var.ssh_private_key_path)
      host        = self.public_ip
    }
  }
}

resource "aws_ami_from_instance" "db_ami" {
  name               = "book-db-ami"
  source_instance_id = aws_instance.db.id
  tags = { Name = "book-db-ami" }
  depends_on = [aws_instance.db]
}

#################################
# 2) App builder -> App AMI     #
#################################
resource "aws_instance" "app_builder" {
  ami                    = data.aws_ami.ubuntu_24.id
  instance_type          = var.instance_type_app
  key_name               = var.key_name
  associate_public_ip_address = true

  tags = { Name = "build-app-image" }

  provisioner "remote-exec" {
    inline = [
      "sudo apt-get update -y",
      "sudo apt-get install -y openjdk-21-jdk maven git",
      "mkdir -p /home/ubuntu/app && cd /home/ubuntu/app",
      "git clone ${var.git_repo_url} app-src || true",
      "cd app-src && git fetch --all && git checkout ${var.git_branch}",
      # build (skip tests για ταχύτητα στα images)
      "mvn -q -DskipTests package",
      # κρατάμε το workspace για να υπάρχει το jar στο AMI
      "echo 'Build finished at ' $(date) | sudo tee /var/log/app-image-build.log"
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file(var.ssh_private_key_path)
      host        = self.public_ip
    }
  }
}

resource "aws_ami_from_instance" "app_ami" {
  name               = "book-app-ami"
  source_instance_id = aws_instance.app_builder.id
  tags = { Name = "book-app-ami" }
  depends_on = [aws_instance.app_builder]
}

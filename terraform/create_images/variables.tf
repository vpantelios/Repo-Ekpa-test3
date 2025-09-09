variable "region" {
  description = "AWS region"
  type        = string
  default     = "eu-central-1"
}

variable "citizen-aws" {
  description = "EC2 Key Pair name"
  type        = string
}

variable "C:\Users\steve-mcqueen\.ssh\citizen-aws" {
  description = "Τοπικό path στο ΙΔΙΩΤΙΚΟ κλειδί για SSH (για remote-exec)"
  type        = string
  default     = "~/.ssh/citizen-aws"
}

variable "instance_type_db" {
  description = "Instance type για το DB image build"
  type        = string
  default     = "t3.micro"
}

variable "instance_type_app" {
  description = "Instance type για το App image build"
  type        = string
  default     = "t3.micro"
}

variable "db_user" {
  type        = string
  default     = "dbuser"
}

variable "db_password" {
  type        = string
  sensitive   = true
}

variable "db_name" {
  type        = string
  default     = "specs"
}

variable "git_repo_url" {
  description = "Git URL"
  type        = string
  default	  = "https://github.com/vpantelios/Repo-Ekpa-test3.git"
}

variable "git_branch" {
  description = "Κλαδί για checkout"
  type        = string
  default     = "main"
}

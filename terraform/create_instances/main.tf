data "terraform_remote_state" "images" {
  backend = "local"
  config = {
    path = "./create_images/terraform.tfstate"
  }
}

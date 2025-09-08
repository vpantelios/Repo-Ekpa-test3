terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region  = var.region
  profile = "citizen"   # ή άστο έξω αν έχεις $env:AWS_PROFILE
}

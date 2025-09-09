output "db_ami_id" {
  description = "AMI ID για τη βάση"
  value       = aws_ami_from_instance.db_ami.id
}

output "app_ami_id" {
  description = "AMI ID για την εφαρμογή"
  value       = aws_ami_from_instance.app_ami.id
}

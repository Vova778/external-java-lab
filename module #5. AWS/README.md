## AWS

### Task

The goal of the exercise to make your familiar with AWS console, base components available in AWS such as Virtual Private Cloud (VPC), Security Group, EC2, RDS and S3. You also are expected to deploy you application within cloud facilities: web application on top of EC2 instance and static assets within S3 bucket. It also required to integrate with managed database instance (RDS). The deployment flow is not expected to be fully automated and most provision is to be done manually.

1. AWS Account Creation AWS gives you an option to create a free account to browse, discover and use the main services it offers. To accomplish the task follow the link AWS Free Tier and complete registration.

2. Application Role Creation In order to access S3 bucket from within your application deployed in EC2 you must make it under authorized identity. The common approach is to assign a service role that we are to create beforehand.
   - Go to IAM section and navigate to Roles section.
   - Under the section Common use cases select EC2 and assign AmazonS3ReadOnlyAccess permission policy on Role. 
   - Save just created Role. 
   
3. Upload Application Jar to AWS S3
   - Create mjc--jar S3 bucket.
   - Make sure the bucket is available for the role created on the step above only. It might be achieved through Bucket Policy. 
   - Upload your application jar file to the newly created bucket. 
   
4. Launch RDS instance
   - Start launching RDS instance available within Free Tier (MySQL or PostgreSQL).
   - Select default VPC, Subnet and Security Group. 
   - Make sure Security Group is configured to permit access by port your database is running on. 
   - Configure your database with application specific data: schema, tables, data. 
   
5. Launch EC2 instance
   - Start launching t2.micro EC2 instance based on Amazon Linux 2 image with default VPC settings.
   - Assign the Role created in step 2.
   - Within User Data section add the script that downloads your application from S3 bucket and launchs it. Use aws cli command: aws s3 cp s3://BUCKET-NAME/FILENAME .
   - In security group settings open the port your application will be running on.
   
6. Terminate or remove all created resources/services once you finished the module and showed a demo.
##  

**General requirements**

Before starting the module please make sure you understand the principles of AWS cloud pricing. Pay attention to resources that you create, the module should be accomplished using AWS Free Tier. It's your responsibility to take care of the costs minimization. If you are not sure about the costs of some resource/service please contact your mentor. Please never commit your account credentials into the git. Do not keep EC2 instances running, stop or terminate instances if you don’t use them.

### Requirements
```
Postman application
```
Use Postman to call the endpoint from postman/rest-aws.postman_collection.json
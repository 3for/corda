Deploying Corda to Corda Testnet from an AWS Cloud Platform VM
====================================================

.. contents::

This document explains how to deploy a Corda node to AWS that can connect directly to the Corda Testnet.
A self service download link can be obtained from the [https://testnet.corda.network](testnet.corda.network). This
document will describe how to set up a virtual machine on the AWS
Cloud Platform to deploy your pre-configured Corda node and automatically connnect
to Testnet.

Pre-requisites
--------------
* Ensure you have a registered Amazon AWS account which can create virtual machines and you are logged on to the AWS console: `https://console.aws.amazon.com`.


Deploy Corda node
-----------------

Browse to `https://console.aws.amazon.com` and log in with your AWS account.


**STEP 1: Launch a new virtual machine**

Click on Launch a virtual machine with EC2.

.. image:: resources/aws-launch.png

In the quick start wizard scroll down and select the most recent Ubuntu machine image as the Amazon Machine Image (AMI).

.. image:: resources/aws_select_ubuntu.png

Select the instance type (for example t2.xlarge). 

.. image:: resources/aws-instance-type.png

Configure a couple of other settings before we review and launch

Under the storage tab (Step 4) increase the storage to 40GB:

.. image:: resources/aws-storage.png

Configure the security group (Step 6) to open the firewall ports which Corda uses.

.. image:: resources/aws-firewall.png

Add a firewall rule for port range 10002-10003 and allow connection from Anywhere. Add another rule for the webserver on port 8080. 

Click on the Review and Launch button then if everything looks ok click Launch.

You will be prompted to set up keys to securely access the VM remotely over ssh. Select "Create a new key pair" from the drop down and enter a name for the key file. Click download to get the keys and keep them safe on your local machine. 

.. image:: resources/aws-keys.png
   :width: 200 px

Click "Launch Instances".

Click on the link to go to the Instances pages in the AWS console where after a few minutes you will be able to see your instance running.

.. image:: resources/aws-instances.png

**STEP 2: Set up static IP address**

On AWS a permenant IP address is called an Elastic IP. Click on the
"Elastic IP" link in the sidenav and then click on "Allocate new address":

.. image:: resources/aws-elastic.png

Follow the form then once the address is allocated click on "Actions"
then "Associate address":

.. image:: resources/aws-elastic-actions.png

Then select the instance you created for your Corda node to attach the
IP address to. 

**STEP 3: Connect to your VM and set up the environment**

In the instances console click on "Connect" and follow the instructions to connect to your instance using ssh.

.. image:: resources/aws-instances-connect.png

.. image:: resources/aws-connect.png


**STEP 4: Download and set up your Corda node**

Now your AWS environment is configured you can switch to the Testnet 
web application and click on the copy to clipboard button to get a one
time installation script:


.. image:: resources/testnet-platform.png
   :scale: 50 %

You can generate as many Testnet identites as you like by refreshing
this page to generate a new one time link. 
	   
In your terminal paste the command you just copied to install and run
your unique Corda instance:

.. code:: bash

    sudo ONE_TIME_DOWNLOAD_KEY=cd6913a4-5390-4956-a544-94148a8c70a7 bash -c "$(curl -L https://testnet.corda.network/api/user/node/install.sh)"

You can now navigate to the external web address of the instance and
see any cordapps running on port 8080 (if you have any installed). 


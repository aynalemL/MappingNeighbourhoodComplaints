# MappingNeighbourhoodComplaints

This project is completed during the Insight Data Engineering program (New York, 2020). 

The main purpose of the project is to map the spatial and temporal distribution of neighborhoods complaint by borough, zip code and neighborhood. And using trend analysisto show the changes in the number of complaints over time (2010-todate) for borough and zipcode.


This project is completed during the Insight Data Engineering program (New York, 2020). 

The main purpose of the project is to map the spatial and temporal distribution of neighborhoods complaint by borough, zip code, and neighborhood. And using trend analysis to show the changes in the number of complaints between 2010-to-date for the specific borough and zip-code.


#Business use cases:

1. To provide renters, homebuyers and real-estate developers an optimized way of learning about the neighborhood in which they are interested. It will serve them as additional information to make an informed decision. It will show them the prevalence of complaints on the specific location over the selected period. 
2. Provide an API for data scientists and the Department of Buildings for analyzing long term trends of complaints in the specific neighbourhood. with a metric:- Total number, year and month of top complaints, consistent complaint type for a given neighborhood, how is it changing over the year, etc... 

#Data Sources

Historical: 311 Service Requests dataset from 2010 to Present (~ 13 GB real data for visualization and ~3000 GB in total after generating additional to stress test the pipline). 

Environment Setup

Manual: 

Prerequisites
AWS account
VPC with DNS Resolution enabled
Subnet in VPC
Security group accepting all inbound and outbound traffic (recommend locking down ports depending on technologies)
AWS Access Key ID and AWS Secret Access Key ID

Install and configure AWS CLI  and Pegasus on local machine. 

Clone the Pegasus project to your local computer and install awscli

$ git clone https://github.com/InsightDataScience/pegasus.git
$ pip install awscli

Databse/Postgres setup:
Follow the instruction in  https://blog.insightdatascience.com/simply-install-postgresql-58c1e4ebf252 to download and setup access to it.


Pipeline

Ingestion:

- The raw data is uploaded and stored on s3. Then converted to parquet in order to make the column that is essential for my project. 



package utils;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class CVStoParquetWriter_ {
       public static void main(String[] args) {
           if(args.length < 3){
               System.out.println("2 arguments are expected [inputFilePath, outputFilePath");
               System.exit(-1);
           }
            CVStoParquetWriter_ csVtoParquetWriter = new CVStoParquetWriter_();

           csVtoParquetWriter.cvsToParquetReadWrite(args[0], args[1],args[2]);

        }


        public static void cvsToParquetReadWrite(String sparkClusterUrl, String inputFilePath, String outputFilePath){

            SparkSession spark = SparkSession.builder().master("local[*]").
                    appName("CVStoParquetConverter").getOrCreate();
            Dataset<Row> complaintData = spark.read().option("header", true).csv(inputFilePath);
            // Read csv file and convert it to parquet
            complaintData.write().option("header",true).parquet(outputFilePath);



       }

 }

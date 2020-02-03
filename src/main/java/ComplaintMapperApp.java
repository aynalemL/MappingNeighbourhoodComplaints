import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.sql.DriverManager;


public class ComplaintMapperApp {
    public static void main(String[] args) throws ClassNotFoundException {
      if (args.length < 6) {
         System.out.println("6 arguments expected [parquedirectory,//dbshost:port/database, dbtable1, dbtable2, dbuser, password]. Try again.");
         System.exit(-1);
     }

        // create sparkSession
        SparkSession spark = SparkSession.builder().
                appName("ComplaintMapperApp").master("local[*]").getOrCreate();

        //read from parquet format
        Dataset<Row> mergedDFParquet = spark.read().option("mergeSchema", true).parquet(args[0]);
        mergedDFParquet.printSchema();

        //create temp view to run sql query
        mergedDFParquet.createOrReplaceTempView("parquetFile");

        Dataset<Row> complaintByTypeByZipByMonth = spark.sql("SELECT IncidentZip, count(1) as count, CONCAT " +
                "(YEAR(TO_DATE(CreatedDate, 'MM/dd/yyyy hh:mm:ss aa') )  ,'-'," +
                " MONTH(TO_DATE(CreatedDate, 'MM/dd/yyyy hh:mm:ss aa')) )" +
                " as month ,ComplaintType FROM parquetFile group by CONCAT(YEAR(TO_DATE(CreatedDate, 'MM/dd/yyyy hh:mm:ss aa') ) " +
                ",'-', MONTH(TO_DATE(CreatedDate, 'MM/dd/yyyy hh:mm:ss aa'))), ComplaintType,IncidentZip");
        complaintByTypeByZipByMonth.show(100, false);

        Dataset<Row> complaintByTypeByBoroughByMonth = spark.sql("SELECT Borough, count(1) as count, CONCAT " +
                "(YEAR(TO_DATE(CreatedDate, 'MM/dd/yyyy hh:mm:ss aa') )  ,'-'," +
                " MONTH(TO_DATE(CreatedDate, 'MM/dd/yyyy hh:mm:ss aa')) )" +
                " as month ,ComplaintType FROM parquetFile group by CONCAT(YEAR(TO_DATE(CreatedDate, 'MM/dd/yyyy hh:mm:ss aa') ) " +
                ",'-', MONTH(TO_DATE(CreatedDate, 'MM/dd/yyyy hh:mm:ss aa'))), ComplaintType,Borough");
        complaintByTypeByBoroughByMonth.show(100, false);


        Class.forName("org.postgresql.Driver");
        complaintByTypeByZipByMonth.write()
                .format("jdbc")
                .option("url",args[1])// "jdbc:postgresql://localhost:5432/complaintmapper?user=*&password=*")//+args[1].trim())// //localhost:5432/testing")
                .option("dbtable",args[2])// "ComplaintByZip")//args[2]) //"public.complaintbyzip")
                .option("user", args[4])
                .option("password",args[5])
                .option("driver", "org.postgresql.Driver")
                .save();

        complaintByTypeByBoroughByMonth.write()
                .format("jdbc")
                .option("url", args[1])//+args[1].trim())// //localhost:5432/testing")
                .option("dbtable", args[3])//args[2]) //"public.complaintbyzip")
                .option("user", args[4])
                .option("password", args[5])
                .option("driver", "org.postgresql.Driver")
                .save();


    }



}





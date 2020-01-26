import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class ComplaintMapperApp {
    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder().master(args[0]).
                appName("ComplaintMapperApp").getOrCreate();

        //
        Dataset<Row> mergedDF = spark.read().option("mergeSchema", true).parquet(args[1]);
      //  mergedDF.printSchema();


        mergedDF.createOrReplaceTempView("parquetFile");

        Dataset<Row> complaintType = spark.sql("SELECT ComplaintType FROM parquetFile");;
        complaintType.show(100,false);

        RelationalGroupedDataset byZip =   mergedDF.groupBy("IncidentZip","ComplaintType");
        byZip.count().show(100, false);




    }

}

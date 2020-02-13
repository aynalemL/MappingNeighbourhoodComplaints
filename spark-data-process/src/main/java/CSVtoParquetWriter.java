import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import utils.CVStoParquetWriter_;

public class CSVtoParquetWriter {

    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("3 arguments are expected [ inputFilePath, outputFilePath");
            System.exit(-1);
        }
        CSVtoParquetWriter csVtoParquetWriter=new CSVtoParquetWriter();
       // CVStoParquetWriter csVtoParquetWriter = new CVStoParquetWriter_();

        csVtoParquetWriter.cvsToParquetReadWrite(args[0], args[1]);

    }
    // grouped.count().withColumnRenamed("count(1)", "counts").write().parquet("output.parquet")

    public static void cvsToParquetReadWrite( String inputFilePath, String outputFilePath){
        SparkSession spark = SparkSession.builder().config("deploy-mode", "cluster").
                appName("CVStoParquetConverter").getOrCreate();
        Dataset<Row> complaintData = spark.read().option("header", true).csv(inputFilePath);
        // Read csv file and convert it to parquet
        complaintData.write().option("header",true).parquet(outputFilePath);
    }
}

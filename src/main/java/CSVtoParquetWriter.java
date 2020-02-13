import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class CSVtoParquetWriter {

    public static void main(String[] args) {
        CSVtoParquetWriter csVtoParquetWriter = new CSVtoParquetWriter();
        csVtoParquetWriter.cvstoParquetReadWrite(args[0], args[1],args[2]);

    }


  public void cvstoParquetReadWrite(String sparkCluster, String inputFilePath, String outputFilePath){

      SparkSession spark = SparkSession.builder().master(sparkCluster).
              appName("CVStoParquetConverter").getOrCreate();
      Dataset<Row> complaintData = spark.read().option("header", true).csv(inputFilePath);
      // Read csv file and convert it to parquet
      complaintData.write().option("header",true).parquet(outputFilePath);

  }

}

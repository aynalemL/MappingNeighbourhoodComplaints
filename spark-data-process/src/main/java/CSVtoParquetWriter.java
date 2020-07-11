import org.apache.commons.io.FileUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class CSVtoParquetWriter {

    static final String regex = "\\d{5}";

    public static void main(String[] args) {
        CSVtoParquetWriter csVtoParquetWriter = new CSVtoParquetWriter();
        //csVtoParquetWriter.cvstoParquetReadWrite(//"spark://localhost",
        //   "/Users/home/MappingNeighbourhoodCompalints-develop/data/Inputd/Input",
        //   "/Users/home/MappingNeighbourhoodCompalints-develop/data/output/newdata555" );
        // csVtoParquetWriter.cvstoParquetReadWrite(args [0], args[1],args[2]);

    }


    public void cvstoParquetReadWrite(String sparkCluster, String inputFilePath, String outputFilePath){

        SparkSession spark = SparkSession.builder().
                appName("CVStoParquetConverter").master(sparkCluster).getOrCreate();
        // spark.sparkContext().addJar("/Users/home/MappingNeighbourhoodCompalints-develop/spark-data-process/target/spark-data-process-1.0-SNAPSHOT.jar");
        // Read csv file and convert it to parquet  and cleaning and data validation


        Dataset<Row> complaintData = spark.read().option("header", true).csv(inputFilePath);


        Dataset<Row> cleanComplaintData = complaintData.filter("Descriptor <>'TraumaCounseling'").filter("CreatedDate <> Null").
                filter("Borough<>Null");

        cleanComplaintData.show(100);

        //(col("Descriptor <>'TraumaCounseling'"));

        try {
            if ( Files.exists(Paths.get(outputFilePath))){
                FileUtils.deleteDirectory(new File(outputFilePath));
                Files.delete(Paths.get(outputFilePath));
            }
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", outputFilePath);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", outputFilePath);
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }

        cleanComplaintData = complaintData.filter(i->isAValidZipCode(i.getString(8)));

        cleanComplaintData.write().option("header",true).parquet(outputFilePath);

    }
    public static boolean isAValidZipCode(String zip) {
        if(zip != null){
            return    Pattern.matches(regex, zip);
        }else{
            return false;
        }

    }

}

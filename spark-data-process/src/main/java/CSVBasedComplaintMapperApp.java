import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.regex.Pattern;

public class CSVBasedComplaintMapperApp {

    static final String regex = "\\d{5}";

    public static void main(String[] args) {
        CSVBasedComplaintMapperApp t = new CSVBasedComplaintMapperApp();
        t.readfromCsv("spark://Aynalems-MBP.fios-router.home:7077",
                "/Users/home/MappingNeighbourhoodCompalints-develop/data/Inputd/Input");
        // "/Users/home/MappingNeighbourhoodCompalints-develop/data/output/yy" );
        // CsvBasedSCompalintApp.readfromCsv(args [0], args[1],args[2]);

    }


    public void readfromCsv(String sparkCluster, String inputFilePath){

        SparkSession spark = SparkSession.builder().
                appName("CSVReader").master(sparkCluster).getOrCreate();
        // spark.sparkContext().addJar("/Users/home/MappingNeighbourhoodCompalints-develop/spark-data-process/target/spark-data-process-1.0-SNAPSHOT.jar");
        // Read csv file and convert it to parquet  and cleaning and data validation


        Dataset<Row> complaintData = spark.read().option("header", true).csv(inputFilePath);


        Dataset<Row> cleanComplaintData = complaintData.filter("Descriptor <>'TraumaCounseling'").filter("CreatedDate <> Null").
                filter("Borough<>Null");

        cleanComplaintData.show(100);

        //(col("Descriptor <>'TraumaCounseling'"));

       /* try {
            if ( Files.exists(Paths.get(inputFilePath))){
                FileUtils.deleteDirectory(new File(inputFilePath));
                Files.delete(Paths.get(inputFilePath));
            }
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", outputFilePath);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", outputFilePath);
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
*/
        //  cleanComplaintData = complaintData.filter(i->isAValidZipCode(i.getString(8)));

        // cleanComplaintData.write().option("header",true).parquet(outputFilePath);
        cleanComplaintData.createOrReplaceTempView("tempView");

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


        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        complaintByTypeByZipByMonth.write()
                .format("jdbc")
                .option("url", "jdbc:postgresql://localhost:5432/complaintmapper")//+args[1].trim())// //localhost:5432/testing")
                .option("dbtable","ComplaintByZip22")//args[2]) //"public.complaintbyzip")
                .option("user", "postgres")
                .option("password","adimpass")
                .option("driver", "org.postgresql.Driver")
                .save();

        complaintByTypeByBoroughByMonth.write()
                .format("jdbc")
                .option("url", "jdbc:postgresql://localhost:5432/complaintmapper")//+args[1].trim())// //localhost:5432/testing")
                .option("dbtable","complaintByBorough")//args[2]) //"public.complaintbyzip")
                .option("user", "postgres")
                .option("password","adimpass")
                .option("driver", "org.postgresql.Driver")
                .save();


    }



    public static boolean isAValidZipCode(String zip) {
        if(zip != null){
            return    Pattern.matches(regex, zip);
        }else{
            return false;
        }

    }

}

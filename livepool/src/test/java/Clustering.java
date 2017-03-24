import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.clustering.LDA;
import org.apache.spark.ml.clustering.LDAModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * Created by: sheng.ke
 * Date: 2016/10/26
 * Time: 下午8:31
 */
public class Clustering {


    public static void main(String[] args) {

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaMultilayerPerceptronClassifierExample")
                .master("local[2]")
                .config("spark.driver.memory", "2G")
                .config("spark.executor.memory", "2G")
                .getOrCreate();

// Loads data.
        Dataset<Row> dataset = spark.read().format("libsvm")
                .load("/Users/jacob/Desktop/datasetFile/simple_feature_selector_v31_15_50/click_train.txt");


        dataset = dataset.limit(1000);
// Trains a k-means model.
        KMeans kmeans = new KMeans().setK(2).setSeed(1234L).setMaxIter(10000);
        KMeansModel model = kmeans.fit(dataset);

        Dataset<Row> result = model.transform(dataset);
// Evaluate clustering by computing Within Set Sum of Squared Errors.
        double WSSSE = model.computeCost(dataset);
        System.out.println("Within Set Sum of Squared Errors = " + WSSSE);


        result.show(50);


    }
}

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

/**
 * Created by: sheng.ke
 * Date: 2016/10/23
 * Time: 下午9:24
 */
public class OneAlgorithmTest {


    public static void main(String[] args) throws IOException {

        List<Integer> layers = Lists.newArrayList(11, 12);

        List<Integer> list = Lists.newArrayList();
        list.add(71);
        list.addAll(layers);
        list.add(2);


        Integer[] clickLayers = list.toArray(new Integer[0]);
//        RandomForestAlgorithm algorithm = new RandomForestAlgorithm();
//
//        algorithm.setDatasetFile(SimpleFeatureSelectorV3.buildDataset());
//
//       Result result= algorithm.trainOnHistoricData();
//
//        System.out.println("======" + result + "==============");
//
//        algorithm.outputFinalResult(FINAL_OUTPUT_FILE_PATH);

    }

    private void saveAndLoad() {

        // Save and load model
//        model.save(sc, "target/tmp/LogisticRegressionModel");
//        LogisticRegressionModel.load(sc, "target/tmp/LogisticRegressionModel");
    }
}

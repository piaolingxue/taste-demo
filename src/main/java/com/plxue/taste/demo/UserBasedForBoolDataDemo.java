/**
 * 
 */
package com.plxue.taste.demo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author matrix
 *
 */
public class UserBasedForBoolDataDemo {
  private static Logger LOG = LoggerFactory.getLogger(UserBasedForBoolDataDemo.class);
  
  /**
   * @param args
   * @throws TasteException 
   */
  public static void main(String[] args) throws IOException, TasteException {
    DataModel dataModel = new FileDataModel(new File("data/bool_data.txt"));
    //UserSimilarity similarity = new TanimotoCoefficientSimilarity(dataModel);
    UserSimilarity similarity = new LogLikelihoodSimilarity(dataModel);
    UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.7, similarity, dataModel);
    Recommender recommender = new GenericBooleanPrefUserBasedRecommender(dataModel, neighborhood, similarity); 
    List<RecommendedItem> recommendions = recommender.recommend(6, 5);
    for (RecommendedItem recommendion : recommendions) {
      LOG.info(String.format("recommendion:%s", recommendion));
    }
  }

}

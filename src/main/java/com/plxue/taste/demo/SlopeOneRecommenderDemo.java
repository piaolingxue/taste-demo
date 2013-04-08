package com.plxue.taste.demo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * Hello world!
 * 
 */
public class SlopeOneRecommenderDemo {
  public static void main(String[] args) throws IOException, TasteException {
    DataModel dataModel = new FileDataModel(new File("data/data.txt"));
    Recommender recommender = new SlopeOneRecommender(dataModel);
    Recommender cacheRecommender = new CachingRecommender(recommender);
    List<RecommendedItem> recommendations = cacheRecommender.recommend(1, 2);
    for (RecommendedItem recommendation : recommendations) {
      System.out.println(recommendation);
    }
    	
  }
}

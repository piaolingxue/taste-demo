package com.plxue.taste.demo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity.ItemItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.GenericUserSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 * 
 */
public class ItemBasedRecommenderDemo {
  private static Logger LOG = LoggerFactory.getLogger(ItemBasedRecommenderDemo.class);
  
  public static void main(String[] args) throws IOException, TasteException {
    DataModel dataModel = new FileDataModel(new File("data/data.txt"));
    Collection<GenericItemSimilarity.ItemItemSimilarity> correlations =
        new HashSet<GenericItemSimilarity.ItemItemSimilarity>();
    ItemSimilarity pearson = new PearsonCorrelationSimilarity(dataModel);
    ItemSimilarity similarity = new GenericItemSimilarity(pearson, dataModel);
    LongPrimitiveIterator iterator = dataModel.getItemIDs();
    long[] itemIDs = new long[5];
    int size = 0;
    while (iterator.hasNext()) {
      if (size == itemIDs.length) {
        long[] newResult = new long[itemIDs.length << 1];
        System.arraycopy(itemIDs, 0, newResult, 0, itemIDs.length);
        itemIDs = newResult;
      }
      itemIDs[size++] = iterator.next();
    }
    if (size != itemIDs.length) {
      long[] newResult = new long[size];
      System.arraycopy(itemIDs, 0, newResult, 0, size);
      itemIDs = newResult;
    }
    for (int i = 0; i < itemIDs.length; ++i) {
      for (int j = i + 1; j < itemIDs.length; ++j) {
        long itemID1 = itemIDs[i];
        long itemID2 = itemIDs[j];
        double value = similarity.itemSimilarity(itemID1, itemID2);
        if (!(value >= -1.0 && value <= 1.0)) {
          continue;
        }
        LOG.info(String.format("itemID1:%d,itemID2:%d,similarity:%.2f", 
          itemID1, itemID2, value));
        correlations.add(new ItemItemSimilarity(itemID1, itemID2, value));
      }
    }
    ItemSimilarity itemSimilarity = new GenericItemSimilarity(correlations);


    Recommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
    Recommender cacheRecommender = new CachingRecommender(recommender);
    List<RecommendedItem> recommendations = cacheRecommender.recommend(1, 2);
    for (RecommendedItem recommendation : recommendations) {
      System.out.println(recommendation);
    }

  }
}

package nlp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import edu.stanford.nlp.util.Pair;
import gem.util.database.DBConnection;
import org.ejml.simple.SimpleMatrix;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

// 0:very negative, 1:negative 2:neutral 3:positive and 4:very positive

public class NLP {
    static StanfordCoreNLP pipeline;

    public static void init() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public static void main(String[] args) {
        NLP nlp = new NLP();
        ArrayList<String> recs = nlp.getRecsAsList();
        NLP.init();
        for(String rec : recs) {
            String[] myRecArray = rec.split(" ");
            if(myRecArray.length<3){continue;}
            ArrayList<Pair> wordpairs = new ArrayList<Pair>();
            for(int i = 0; i < myRecArray.length - 2; i++){
                String thisWord = myRecArray[i];
                String nextWord = myRecArray[i+1];
                String thirdWord = myRecArray[i+2];
                Pair<Integer, String[]> pair = new Pair<>(1, new String[]{thisWord,nextWord,thirdWord});
                wordpairs.add(pair);



            }
        //    System.out.println(rec);
           for(Pair p : wordpairs)
           {

               ArrayList l = (ArrayList) p.asList();
               String[] k = (String[]) l.get(1);
             //  System.out.println(k[0] +" "+k[1]+": "+NLP.findSentiment(k[0] +" "+k[1]));
               if(NLP.findSentiment(k[0] +" "+k[1]+" "+k[2]) < 1 )
               {
                   System.out.println(k[0] +" "+k[1]+" "+k[2]+": "+NLP.findSentiment(k[0] +" "+k[1]+" "+k[2]));
               }
           }
        }
    }

    public static int findSentiment(String inputtext) {

        int mainSentiment = 0;
        if (inputtext != null && inputtext.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(inputtext);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
           //     SimpleMatrix sentiment_new = RNNCoreAnnotations.getPredictions(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
        return mainSentiment;
    }

    private ArrayList<String> getRecsAsList() {

   ArrayList<String> recsList = new ArrayList<>();

    String conditionalContentQuery = "select doc_text from ytex.document";

            try {
        DBConnection dbConnection = new DBConnection();
        PreparedStatement ps = dbConnection.getDataBaseConnection().prepareStatement(conditionalContentQuery);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            recsList.add(FilterSearchText(rs.getString(1)));
        }
        ps.close();
        dbConnection.close();
    } catch (
    SQLException e) {
        e.printStackTrace();
    }
          return recsList;
    }
    private String FilterSearchText(String text)
    {
        String filteredText = "";

        filteredText = text.replaceAll("\\b[a-zA-z]{1,3}\\b\\s?", "");
        filteredText = filteredText.replaceAll("\\b[0-9-]{1,}\\b\\s?", "");
        //  filteredText=filteredText.replaceAll("(?i)for","");
        filteredText = filteredText.replaceAll("(?i);", "");
        filteredText = filteredText.replaceAll("(?i)<", "");
        filteredText = filteredText.replaceAll("(?i)>", "");
        filteredText = filteredText.replaceAll("(?i)=", "");
        filteredText = filteredText.replaceAll("(?i)\\.", "");
        filteredText = filteredText.replaceAll("(?i)-", "");
        filteredText = filteredText.replaceAll("(?i)\\?", "");
        filteredText = filteredText.replaceAll("(?i):", "");
        filteredText = filteredText.replaceAll("(?i)/", " ");
        filteredText = filteredText.replaceAll("(?i)\"", "");
        filteredText = filteredText.replaceAll("(?i)\\bhave\\b", " ");
        filteredText = filteredText.replaceAll("(?i)\\bfrom\\b", " ");
        filteredText = filteredText.replaceAll("“", "");
        filteredText = filteredText.replaceAll("”", "");
        //   filteredText=filteredText.replaceAll("(?i)can","");
        //   filteredText=filteredText.replaceAll("(?i)and","");
        //   filteredText=filteredText.replaceAll("(?i)are","");
        //   filteredText=filteredText.replaceAll("(?i)the","");
        filteredText = filteredText.replaceAll("(?i)\\bthis\\b", " ");
        filteredText = filteredText.replaceAll("(?i)\\bthat\\b", " ");
        filteredText = filteredText.replaceAll("(?i)\\bwith\\b", " ");
        filteredText = filteredText.replaceAll("(?i)\\bwho\\b", " ");
        filteredText = filteredText.replaceAll("(?i)\\bwhom\\b", " ");
        filteredText = filteredText.replaceAll("(?i)\\bwhich\\b", " ");
        filteredText = filteredText.replaceAll("(?i)\\bwhere\\b", " ");
        filteredText = filteredText.replaceAll("\\s+", " ");
        //    filteredText=filteredText.replaceAll("\\(|\\)|\\,|\\.","");
        //  filteredText=filteredText.replaceAll("\\b[a-z-A-Z()<>]{1,2}\\b\\s?", "");
        filteredText = filteredText.replaceAll("[^a-zA-Z]{3,}", " ");
        return filteredText.trim();
    }

}
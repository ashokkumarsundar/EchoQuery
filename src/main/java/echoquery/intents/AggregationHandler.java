package echoquery.intents;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

import echoquery.sql.Querier;
import echoquery.sql.QueryRequest;
import echoquery.sql.QueryResult;
import echoquery.sql.SingletonConnection;
import echoquery.utils.Response;

public class AggregationHandler implements IntentHandler {

  private static final Logger log =
      LoggerFactory.getLogger(AggregationHandler.class);
  private static final Querier querier =
      new Querier(SingletonConnection.getInstance());

  @Override
  public SpeechletResponse respond(Intent intent, Session session) {
    return Response.say(getResponseInEnglish(intent, session));
  }

  /**
   * Exposed for testing purposes - SpeechletResponse is impossible to inspect.
   */
  public String getResponseInEnglish(Intent intent, Session session) {
    try {
      QueryResult result = querier.execute(QueryRequest.of(intent));
      return result.toEnglish();
    } catch (SQLException e) {
      log.info("StatementCreationError: " + e.getMessage());
      return "There was an error querying the database.";
    }
  }
}
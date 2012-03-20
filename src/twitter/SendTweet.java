package twitter;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import java.util.logging.Logger;


public final class SendTweet {
	
	private SendTweet () {
				
	}
	
    public static void publish(String message){
    	final Logger logger = Logger.getLogger(SendTweet.class.getName());
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            try {
                RequestToken requestToken = twitter.getOAuthRequestToken();
                AccessToken accessToken = null;
                while (null == accessToken) {
                    logger.fine("Open the following URL and grant access to your account:");
                    requestToken.getAuthorizationURL();
                    try {
                            accessToken = twitter.getOAuthAccessToken(requestToken);
                    } catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            logger.severe("Unable to get the access token.");
                        } else {
                            te.printStackTrace();
                        }
                    }
                }
                logger.info("Got access token.");
                logger.info("Access token: " + accessToken.getToken());
                logger.info("Access token secret: " + accessToken.getTokenSecret());
            } catch (IllegalStateException ie) {
                // access token is already available, or consumer key/secret is not set.
                if (!twitter.getAuthorization().isEnabled()) {
                    logger.severe("OAuth consumer key/secret is not set.");
                    return;
                }
            }
            Status status = twitter.updateStatus(message);
            logger.info("Successfully updated the status to [" + status.getText() + "].");
        } catch (TwitterException te) {
            te.printStackTrace();
            logger.severe("Failed to get timeline: " + te.getMessage());
        } 
     }
	

}

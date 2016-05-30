package rs.tickettracker.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import model.League;
import model.Match;
import model.Status;
import rs.tickettracker.activities.MainActivity;

/**
 * Created by gisko on 29-Apr-16.
 */
public class LiveScoreAPIHelper {

    public static Match getMatchUpdate(long ticketId, long matchServiceId, long matchId, boolean showGoalsNotification, Context context) {
        String baseUrl = "http://api.football-data.org/v1/fixtures/" + matchServiceId;
        JSONObject serviceResult = requestWebService(baseUrl, "n1");
        Match m = Match.load(Match.class, matchId);
        try {
            if (serviceResult.has("fixture")) {
                JSONObject match = serviceResult.getJSONObject("fixture");
                if (match == null)
                    return m;
                String status = match.getString("status");
                if (status.equals("FINISHED")) {
                    m.isFinished = true;
                } else {
                    m.isFinished = false;
                }
                int homeScore = match.getJSONObject("result").optInt("goalsHomeTeam", -1);
                int awayScore = match.getJSONObject("result").optInt("goalsAwayTeam", -1);
                if (m.homeScore != homeScore) {
                    if (homeScore != 0) {
                        m.homeScore = homeScore;
                        if (showGoalsNotification) {
                            Intent ints = new Intent(MainActivity.SYNC_DATA);
                            ints.putExtra("ticketId", ticketId);
                            ints.putExtra("MESSAGE_TEXT", m.homeTeam + " scored. Current result is: [" + m.homeScore + "]:" + m.awayScore);
                            context.sendBroadcast(ints);
                        }
                    } else {
                        m.homeScore = 0;
                    }
                }
                if (m.awayScore != awayScore) {
                    if (awayScore != 0) {
                        m.awayScore = awayScore;
                        if (showGoalsNotification) {
                            Intent ints = new Intent(MainActivity.SYNC_DATA);
                            ints.putExtra("ticketId", ticketId);
                            ints.putExtra("MESSAGE_TEXT", m.awayTeam + " scored. Current result is: " + m.homeScore + ":[" + m.awayScore + "]");
                            context.sendBroadcast(ints);
                        }
                    } else {
                        m.awayScore = 0;
                    }
                }
                m.save();
            }
        } catch (JSONException e) {
            Log.i("API", "Get your API key for free");
        }
        return m;
    }

    public static List<Match> findAllMatchesForLeague(int day, long league) {
        String baseUrl = "http://api.football-data.org/v1/soccerseasons/" + league + "/fixtures";
        String timeFrame = "";
        if (day > 0) {
            timeFrame = "n" + day;
        } else if (day < 0) {
            timeFrame = "p" + Math.abs(day);
        }

        JSONObject serviceResult = requestWebService(baseUrl, timeFrame);
        League ligue = new Select().from(League.class).where("leagueServisId = ?", league).executeSingle();
        Status matchStatus = new Select().from(Status.class).where("status =?", "Active").executeSingle();
        List<Match> foundMatches = new ArrayList<Match>();

        try {
            if (serviceResult == null)
                return foundMatches;
            if (serviceResult.has("fixtures")) {
                JSONArray matches = serviceResult.getJSONArray("fixtures");
                if (matches == null)
                    return foundMatches;
                for (int i = 0; i < matches.length(); i++) {
                    JSONObject match = matches.getJSONObject(i);
                    Match m = new Match();
                    m.awayTeam = match.getString("awayTeamName");
                    m.homeTeam = match.getString("homeTeamName");
                    m.homeScore = match.getJSONObject("result").optInt("goalsHomeTeam", -1);
                    m.awayScore = match.getJSONObject("result").optInt("goalsAwayTeam", -1);
                    String dateStart = match.getString("date");
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = format.parse(dateStart);
                    m.gameStart = date;
                    m.league = ligue;
                    String status = match.getString("status");
                    if (status.equals("FINISHED")) {
                        m.isFinished = true;
                    } else {
                        m.isFinished = false;
                    }
                    m.status = matchStatus;
                    m.matchServisId = match.getLong("id");
                    foundMatches.add(m);
                }
            }
        } catch (JSONException e) {
            Log.i("API", "Get your API key for free");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return foundMatches;
    }


    public static JSONObject requestWebService(String serviceUrl, String timeFrame) {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            Uri myCustomUri = Uri.parse(serviceUrl)
                    .buildUpon() // returns Uri.Builder
                    .appendQueryParameter("timeFrame", timeFrame)
                    .build();


            URL urlToRequest = new URL(myCustomUri.toString());
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setRequestProperty("X-Auth-Token", "4b856c946312488783ea2593e32f3a97");
            urlConnection.setRequestProperty("X-Response-Control", "minified");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            return new JSONObject(getResponseText(in));

        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        } catch (JSONException e) {
            // response body is no valid JSON string
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

}

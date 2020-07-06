/**
 *
 *  @author Werczyński Piotr S17093
 *
 */

package zad1;



import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;


public class Service {

    private Locale kraj = Locale.ENGLISH;
    private Currency waluta = Currency.getInstance("GBP");

    public Service(String kraj) {
        Locale[] locales = Locale.getAvailableLocales();

        for (int i = 0; i < locales.length; i++) {
            if (locales[i].getDisplayCountry(new Locale("ENGLISH", "US")).equals(kraj)) {
                waluta = Currency.getInstance(locales[i]);
                this.kraj = locales[i];
            }
        }
    }

    public String getWeather(String miasto) {
        URL json;
        try {
            json = new URL("http://api.openweathermap.org/data/2.5/find?q=" +miasto+","+kraj+"&units=metric&appid=53f8c1339cee58028dcd3f6e05fb6d1b");
            return IOUtils.toString(json.openStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getRateFor(String kodWaluty) {
        double rate = 0;
        try {
            URL json = new URL("https://api.exchangeratesapi.io/latest?symbols="+kodWaluty+"&base="+waluta.getCurrencyCode());
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(IOUtils.toString(json.openStream()));
            JSONObject rates = (JSONObject) jsonObject.get("rates");

            return (double)rates.get(kodWaluty);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            rate = 0;
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return rate;
    }

    public Double getNBPRate() {

        double rate=0;
        try {
            URL json = new URL("http://api.nbp.pl/api/exchangerates/rates/a/"+waluta.getCurrencyCode()+"/?format=json");
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(IOUtils.toString(json.openStream()));
            JSONArray ratesTable = (JSONArray) jsonObject.get("rates");
            JSONObject rateObj = (JSONObject) ratesTable.get(0);

            return (double) rateObj.get("mid");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            rate=1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rate;
    }

    public Currency getWaluta() {
        return waluta;
    }
}

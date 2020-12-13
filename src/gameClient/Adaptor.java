package gameClient;

import api.GeoLocation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Adaptor {

    public class pokemonInfo {
        GeoLocation position;
        double value;
        int type;

        public pokemonInfo(String file){
            Reader reader;
            try {
                reader = Files.newBufferedReader(Paths.get(file));
                JsonObject obj= JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray jsonArray=obj.get("Pokemons").getAsJsonObject().get("Pokemon").getAsJsonObject().getAsJsonArray();
                String p=jsonArray.get(0).getAsJsonObject().get("pos").getAsString();
                double[] location=new double[3];
                int i=0;
                for(String geo : p.split(",")){
                    location[i]=Double.parseDouble(geo);
                    i++;
                }
                position=new GeoLocation(location[0],location[1],location[1]);
                value=obj.get("value").getAsDouble();
                type=obj.get("type").getAsInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

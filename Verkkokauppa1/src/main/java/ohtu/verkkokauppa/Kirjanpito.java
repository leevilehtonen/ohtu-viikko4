package ohtu.verkkokauppa;

import java.util.ArrayList;

public interface Kirjanpito {
    void lisaaTapahtuma(String tapahtuma);

    ArrayList<String> getTapahtumat();
}

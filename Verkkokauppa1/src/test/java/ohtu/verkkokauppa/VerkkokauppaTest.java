package ohtu.verkkokauppa;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class VerkkokauppaTest {


    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan() {
        // luodaan ensin mock-oliot
        Pankki pankki = mock(Pankki.class);
        
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        // määritellään että viitegeneraattori palauttaa viitten 42
        when(viite.uusi()).thenReturn(42);
    
        Varasto varasto = mock(Varasto.class);
        // määritellään että tuote numero 1 on maito jonka hinta on 5 ja saldo 10
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
    
        // sitten testattava kauppa 
        Kauppa k = new Kauppa(varasto, pankki, viite);              
    
        // tehdään ostokset
        k.aloitaAsiointi();
        k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
        k.tilimaksu("pekka", "12345");
    
        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
        verify(pankki).tilisiirto(anyString(), anyInt(), anyString(), anyString(),anyInt());   
        // toistaiseksi ei välitetty kutsussa käytetyistä parametreista
    }

    @Test
    public void yhdenSaldollisenTuotteenOstoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan() {
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        Varasto varasto = mock(Varasto.class);
        

        when(viite.uusi()).thenReturn(123);
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
    
        Kauppa k = new Kauppa(varasto, pankki, viite);                  
        k.aloitaAsiointi();
        k.lisaaKoriin(1); 
        k.tilimaksu("pekka", "12345");
    
        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
        verify(pankki).tilisiirto(eq("pekka"), eq(123), eq("12345"), eq("33333-44455"), eq(5)); 

    }

    @Test
    public void kahdenEriSaldollisenTuotteenOstoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan() {
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        Varasto varasto = mock(Varasto.class);
        

        when(viite.uusi()).thenReturn(123);
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.saldo(2)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "kerma", 6));
    
        Kauppa k = new Kauppa(varasto, pankki, viite);                  
        k.aloitaAsiointi();
        k.lisaaKoriin(1); 
        k.lisaaKoriin(2); 
        k.tilimaksu("pekka", "12345");
    
        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
        verify(pankki).tilisiirto(eq("pekka"), eq(123), eq("12345"), eq("33333-44455"), eq(11)); 

    }

    @Test
    public void kahdenSamanSaldollisenTuotteenOstoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan() {
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        Varasto varasto = mock(Varasto.class);
        

        when(viite.uusi()).thenReturn(123);
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
    
        Kauppa k = new Kauppa(varasto, pankki, viite);                  
        k.aloitaAsiointi();
        k.lisaaKoriin(1); 
        k.lisaaKoriin(1); 
        k.tilimaksu("pekka", "12345");
    
        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
        verify(pankki).tilisiirto(eq("pekka"), eq(123), eq("12345"), eq("33333-44455"), eq(10)); 

    }


    @Test
    public void kahdenEriSaldollisenJaSaldottomanTuotteenOstoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan() {
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        Varasto varasto = mock(Varasto.class);
        

        when(viite.uusi()).thenReturn(123);
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.saldo(2)).thenReturn(0); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "kerma", 6));
    
        Kauppa k = new Kauppa(varasto, pankki, viite);                  
        k.aloitaAsiointi();
        k.lisaaKoriin(1); 
        k.lisaaKoriin(2); 
        k.tilimaksu("pekka", "12345");
    
        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
        verify(pankki).tilisiirto(eq("pekka"), eq(123), eq("12345"), eq("33333-44455"), eq(5)); 

    }

    
}
/*
aloitetaan asiointi, koriin lisätään tuote, jota varastossa on ja suoritetaan ostos, eli kutsutaan metodia kaupan tilimaksu(). varmistettava että kutsutaan pankin metodia tilisiirto oikealla asiakkaalla, tilinumerolla ja summalla
tämä siis on muuten copypaste esimerkistä, mutta verify:ssä on tarkastettava että parametreilla on oikeat arvot
aloitetaan asiointi, koriin lisätään kaksi eri tuotetta, joita varastossa on ja suoritetaan ostos. varmistettava että kutsutaan pankin metodia tilisiirto oikealla asiakkaalla, tilinumerolla ja summalla
aloitetaan asiointi, koriin lisätään kaksi samaa tuotetta jota on varastossa tarpeeksi ja suoritetaan ostos. varmistettava että kutsutaan pankin metodia tilisiirto oikealla asiakkaalla, tilinumerolla ja summalla
aloitetaan asiointi, koriin lisätään tuote jota on varastossa tarpeeksi ja tuote joka on loppu ja suoritetaan ostos. varmistettava että kutsutaan pankin metodia tilisiirto oikealla asiakkaalla, tilinumerolla ja summalla
Kaikkien testien tarkastukset onnistuvat mockiton verify-komennolla.
*/
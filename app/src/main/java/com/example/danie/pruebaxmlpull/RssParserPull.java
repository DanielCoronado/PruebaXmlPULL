package com.example.danie.pruebaxmlpull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

/**
 * Created by danie on 29-08-2016.
 */
public class RssParserPull {
    private URL rssUrl;

    //recibe como parametro la url del documento xml
    public RssParserPull(String url)
    {
        try
        {
            this.rssUrl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<Noticia> parse()
    {
        List<Noticia> noticias = null;
        XmlPullParser parser = Xml.newPullParser();

        try
        {
            //parsear el fichero
            parser.setInput(this.getInputStream(), null);
            //obtiene un tipo de evento
            int evento = parser.getEventType();

            Noticia noticiaActual = null;


            while (evento != XmlPullParser.END_DOCUMENT)//el evento es distinto de (eventos no disponibles)
            {
                String etiqueta = null;

                switch (evento)
                {
                    //en el caso de que el analisis este al principio y todavia no a sido leido
                    case XmlPullParser.START_DOCUMENT:

                        noticias = new ArrayList<Noticia>();
                        break;
                    //en caso de que si es xml se leyo
                    case XmlPullParser.START_TAG:
                        //obtiene los nombre de las variables
                        etiqueta = parser.getName();

                        if (etiqueta.equals("item"))
                        {
                            noticiaActual = new Noticia();
                        }
                        else if (noticiaActual != null)
                        {
                            if (etiqueta.equals("pubDate"))
                            {
                                //nexttext(): devuelve el contenido del elemento
                                noticiaActual.setFecha(parser.nextText());
                            }
                            else if (etiqueta.equals("title"))
                            {
                                noticiaActual.setTitulo(parser.nextText());
                            }

                        }
                        break;
                    //se leyo el xml
                    //se puede obtener denuevo con getname
                    case XmlPullParser.END_TAG:

                        etiqueta = parser.getName();

                        if (etiqueta.equals("item") && noticiaActual != null)
                        {
                            noticias.add(noticiaActual);
                        }
                        break;
                }

                evento = parser.next();
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return noticias;
    }

    //se conecta con la url especifica
    private InputStream getInputStream()
    {
        try
        {
            return rssUrl.openConnection().getInputStream();// abre coneccion y obtiene el stream
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}

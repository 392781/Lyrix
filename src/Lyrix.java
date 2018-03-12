import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lyrix {
    private static String API_KEY;
    private Mp3File mp3;
    private MusixMatch searcher;

    public Lyrix(String MP3Name) {
        setMP3(MP3Name);
        getAPI_KEY();
        searcher = new MusixMatch(API_KEY);
    }

    public boolean searchAndReplace() {
        try {
            ID3v2 tag = mp3.getId3v2Tag();

            String trackName = tag.getTitle();
            String trackArtist = tag.getArtist();

            Track trackSearch = searcher.getMatchingTrack(trackName, trackArtist);
            TrackData data = trackSearch.getTrack();

            Lyrics lyrics = searcher.getLyrics(data.getTrackId());

            tag.setLyrics(lyrics.getLyricsBody());


        } catch (MusixMatchException e) {
            System.out.println("Couldn't find lyrics");
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public boolean setMP3(String MP3Name) {
        try {
            mp3 = new Mp3File(MP3Name);
        } catch (IOException | InvalidDataException | UnsupportedTagException e) {
            System.out.println("Invalid MP3 File Name");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private static void getAPI_KEY() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("API_KEY/API_KEY.txt"));
            API_KEY = reader.readLine();
        } catch (IOException e) {
            System.out.println("Invalid File Location");
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        Lyrix lyrix = new Lyrix("src/01. Divinity.mp3");
        System.out.println(lyrix.searchAndReplace());
    }
}
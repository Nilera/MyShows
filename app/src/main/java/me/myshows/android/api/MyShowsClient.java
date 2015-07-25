package me.myshows.android.api;

import java.util.List;

import me.myshows.android.api.impl.Credentials;
import me.myshows.android.entity.NextEpisode;
import me.myshows.android.entity.Show;
import me.myshows.android.entity.UnwatchedEpisode;
import me.myshows.android.entity.User;
import me.myshows.android.entity.UserEpisode;
import me.myshows.android.entity.UserShow;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by warrior on 27.06.15.
 */
public interface MyShowsClient {

    void setObserverScheduler(Scheduler scheduler);

    boolean hasCredentials();

    Observable<Boolean> authentication(Credentials credentials);

    Observable<User> profile();

    Observable<List<UserShow>> profileShows();

    Observable<List<UserEpisode>> profileEpisodesOfShow(int showId);

    Observable<List<UnwatchedEpisode>> profileUnwatchedEpisodes();

    Observable<List<NextEpisode>> profileNextEpisodes();

    Observable<Show> showInformation(int showId);
}

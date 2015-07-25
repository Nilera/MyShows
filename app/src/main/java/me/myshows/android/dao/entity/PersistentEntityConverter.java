package me.myshows.android.dao.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import me.myshows.android.entity.Episode;
import me.myshows.android.entity.NextEpisode;
import me.myshows.android.entity.Show;
import me.myshows.android.entity.Statistics;
import me.myshows.android.entity.UnwatchedEpisode;
import me.myshows.android.entity.User;
import me.myshows.android.entity.UserEpisode;
import me.myshows.android.entity.UserShow;
import me.myshows.android.serialization.Marshaller;

public class PersistentEntityConverter {

    private final Marshaller marshaller;

    public PersistentEntityConverter(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    @SuppressWarnings("unchecked")
    public User toUser(PersistentUser persistentUser) {
        try {
            List<User> friends = marshaller.deserialize(persistentUser.getFriends(), List.class);
            List<User> followers = marshaller.deserialize(persistentUser.getFollowers(), List.class);
            Statistics stats = marshaller.deserialize(persistentUser.getStats(), Statistics.class);
            return new User(persistentUser.getLogin(), persistentUser.getAvatarUrl(),
                    persistentUser.getWastedTime(), persistentUser.getGender(),
                    friends, followers, stats);
        } catch (IOException e) {
            throw new RuntimeException("Unreachable state");
        }
    }

    public PersistentUser fromUser(User user) {
        try {
            byte[] friends = marshaller.serialize(user.getFriends());
            byte[] followers = marshaller.serialize(user.getFollowers());
            byte[] stats = marshaller.serialize(user.getStats());
            return new PersistentUser(user.getLogin(), user.getAvatarUrl(), user.getWastedTime(),
                    user.getGender(), friends, followers, stats);
        } catch (IOException e) {
            throw new RuntimeException("Unreachable state");
        }
    }

    public UserShow toUserShow(PersistentUserShow persistentUserShow) {
        return new UserShow(persistentUserShow.getShowId(), persistentUserShow.getTitle(),
                persistentUserShow.getRuTitle(), persistentUserShow.getRuntime(),
                persistentUserShow.getShowStatus(), persistentUserShow.getWatchStatus(),
                persistentUserShow.getWatchedEpisodes(), persistentUserShow.getTotalEpisodes(),
                persistentUserShow.getRating(), persistentUserShow.getImage());
    }

    public PersistentUserShow fromUserShow(UserShow userShow) {
        return new PersistentUserShow(userShow.getShowId(), userShow.getTitle(),
                userShow.getRuTitle(), userShow.getRuntime(), userShow.getShowStatus(),
                userShow.getWatchStatus(), userShow.getWatchedEpisodes(), userShow.getTotalEpisodes(),
                userShow.getRating(), userShow.getImage());
    }

    public UserEpisode toUserEpisode(PersistentUserEpisode persistentUserEpisode) {
        return new UserEpisode(persistentUserEpisode.getId(),
                persistentUserEpisode.getWatchDate(), persistentUserEpisode.getRating());
    }

    public PersistentUserEpisode fromUserEpisode(UserEpisode userEpisode) {
        return new PersistentUserEpisode(userEpisode.getId(), userEpisode.getWatchDate(),
                userEpisode.getRating());
    }

    public NextEpisode toNextEpisodePreview(PersistentNextEpisode persistentNextEpisode) {
        return new NextEpisode(persistentNextEpisode.getEpisodeId(),
                persistentNextEpisode.getTitle(), persistentNextEpisode.getShowId(),
                persistentNextEpisode.getSeasonNumber(), persistentNextEpisode.getEpisodeNumber(),
                persistentNextEpisode.getAirDate());
    }

    public PersistentNextEpisode fromNextEpisodePreview(NextEpisode nextEpisode) {
        return new PersistentNextEpisode(nextEpisode.getEpisodeId(),
                nextEpisode.getTitle(), nextEpisode.getShowId(),
                nextEpisode.getSeasonNumber(), nextEpisode.getEpisodeNumber(),
                nextEpisode.getAirDate());
    }

    public UnwatchedEpisode toUnwatchedEpisodePreview(PersistentUnwatchedEpisode persistentNextEpisodePreview) {
        return new UnwatchedEpisode(persistentNextEpisodePreview.getEpisodeId(),
                persistentNextEpisodePreview.getTitle(), persistentNextEpisodePreview.getShowId(),
                persistentNextEpisodePreview.getSeasonNumber(), persistentNextEpisodePreview.getEpisodeNumber(),
                persistentNextEpisodePreview.getAirDate());
    }

    public PersistentUnwatchedEpisode fromUnwatchedEpisodePreview(UnwatchedEpisode nextEpisodePreview) {
        return new PersistentUnwatchedEpisode(nextEpisodePreview.getEpisodeId(),
                nextEpisodePreview.getTitle(), nextEpisodePreview.getShowId(),
                nextEpisodePreview.getSeasonNumber(), nextEpisodePreview.getEpisodeNumber(),
                nextEpisodePreview.getAirDate());
    }

    public Episode toEpisode(PersistentEpisode persistentEpisode) {
        return new Episode(persistentEpisode.getId(), persistentEpisode.getTitle(),
                persistentEpisode.getSequenceNumber(), persistentEpisode.getSeasonNumber(),
                persistentEpisode.getEpisodeNumber(), persistentEpisode.getAirDate(),
                persistentEpisode.getShortName(), persistentEpisode.getTvrageLink(),
                persistentEpisode.getImage(), persistentEpisode.getProductionNumber());
    }

    public PersistentEpisode fromEpisode(Episode episode) {
        return new PersistentEpisode(episode.getId(), episode.getTitle(), episode.getSeasonNumber(),
                episode.getEpisodeNumber(), episode.getAirDate(), episode.getShortName(),
                episode.getTvrageLink(), episode.getImage(), episode.getProductionNumber(),
                episode.getSequenceNumber());
    }

    public Show toShow(PersistentShow persistentShow) {
        try {
            int[] genres = marshaller.deserialize(persistentShow.getGenres(), int[].class);
            Map<String, Episode> episodes = toEpisodeMap(persistentShow.getEpisodes());
            String[] images = marshaller.deserialize(persistentShow.getImages(), String[].class);
            return new Show(persistentShow.getId(), persistentShow.getTitle(), persistentShow.getRuTitle(),
                    persistentShow.getStatus(), persistentShow.getCountry(), persistentShow.getStarted(),
                    persistentShow.getEnded(), persistentShow.getYear(), persistentShow.getKinopoiskId(),
                    persistentShow.getTvrageId(), persistentShow.getImdbId(), persistentShow.getVoted(),
                    persistentShow.getRating(), persistentShow.getRuntime(), persistentShow.getImage(),
                    genres, episodes, persistentShow.getWatching(), images, persistentShow.getDescription());
        } catch (IOException e) {
            throw new RuntimeException("Unreachable state");
        }
    }

    public PersistentShow fromShow(Show show) {
        try {
            byte[] genres = marshaller.serialize(show.getGenres());
            RealmList<PersistentEpisode> episodes = fromEpisodeMap(show.getEpisodes());
            byte[] images = marshaller.serialize(show.getImages());
            return new PersistentShow(show.getId(), show.getTitle(), show.getRuTitle(),
                    show.getStatus(), show.getCountry(), show.getStarted(),
                    show.getEnded(), show.getYear(), show.getKinopoiskId(),
                    show.getTvrageId(), show.getImdbId(), show.getVoted(),
                    show.getRating(), show.getRuntime(), show.getImage(),
                    genres, episodes, show.getWatching(), images, show.getDescription());
        } catch (IOException e) {
            throw new RuntimeException("Unreachable state");
        }
    }

    private Map<String, Episode> toEpisodeMap(RealmList<PersistentEpisode> persistentEpisodes) {
        if (persistentEpisodes == null) {
            return null;
        }
        Map<String, Episode> episodes = new HashMap<>();
        for (PersistentEpisode persistentEpisode : persistentEpisodes) {
            Episode episode = toEpisode(persistentEpisode);
            episodes.put(String.valueOf(episode.getId()), episode);
        }
        return episodes;
    }

    private RealmList<PersistentEpisode> fromEpisodeMap(Map<String, Episode> episodes) {
        if (episodes == null) {
            return null;
        }
        RealmList<PersistentEpisode> persistentEpisodes = new RealmList<>();
        for (Episode episode : episodes.values()) {
            persistentEpisodes.add(fromEpisode(episode));
        }
        return persistentEpisodes;
    }
}

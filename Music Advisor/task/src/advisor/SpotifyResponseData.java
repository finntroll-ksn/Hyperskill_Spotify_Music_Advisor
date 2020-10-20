package advisor;

class SpotifyResponseData {
    private String album = null;
    private String artists = null;
    private String category = null;
    private String link = null;

    void setAlbum(String album) {
        this.album = album;
    }

    void setArtists(String authors) {
        this.artists = authors;
    }

    void setCategory(String category) {
        this.category = category;
    }

    void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {

        StringBuilder info = new StringBuilder();

        if (album != null) {
            info.append(album).append("\n");
        }

        if (artists != null) {
            info.append(artists).append("\n");
        }

        if (link != null) {
            info.append(link).append("\n");
        }

        if (category != null) {
            info.append(category).append("\n");
        }

        return info.toString().replaceAll("\"", "");
    }
}

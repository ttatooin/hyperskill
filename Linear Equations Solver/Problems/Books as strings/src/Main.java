class Book {

    private String title;
    private int yearOfPublishing;
    private String[] authors;

    public Book(String title, int yearOfPublishing, String[] authors) {
        this.title = title;
        this.yearOfPublishing = yearOfPublishing;
        this.authors = authors;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("title=" + title);
        builder.append(",");
        builder.append("yearOfPublishing" + yearOfPublishing);
        builder.append(",");
        builder.append("authors=[");
        for (int k = 0; k < authors.length - 1; ++k) {
            builder.append(authors[k] + ",");
        }
        builder.append(authors[authors.length - 1] + "]");
        return builder.toString();
    }
}

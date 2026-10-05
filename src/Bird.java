public class Bird {
    String species,family,order;

    public Bird(String species, String family, String order){
        this.species=species;
        this.family=family;
        this.order=order;
    }

    public Bird(String species){
        this.species=species;
    }

    public String getType(QuizType type){
        switch (type){
            case SPECIES-> {
                return species;
            }
            case FAMILY -> {
                return family;
            }
            default -> {
                return order;
            }
        }
    }
    @Override
    public boolean equals(Object o){
        if (this==o)return true;
        if (!(o instanceof Bird))return false;
        Bird other=(Bird)o;
        return this.species.equals(other.species);
    }
}

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class OsmWayIterator implements Iterator<OsmWay> {

    //private Map<Long, OsmWay> map;
    private Iterator<Map.Entry<Long, OsmWay>> it;
    private Function<Map<String, String>, Boolean> filter;

    public OsmWayIterator(Map<Long, OsmWay> map) {
        this.it = map.entrySet().iterator();
        this.filter = x -> true;
    }

    public void SetFilter(Function<Map<String, String>, Boolean> filter) {
        this.filter = filter;
    }

    @Override
    public void remove() {
        it.remove();
    }


    @Override
    public boolean hasNext() {

        if (!it.hasNext()) return false;
        Map.Entry<Long, OsmWay> next;
        do {
            next = it.next();
        } while (it.hasNext() && !filter.apply(next.getValue().getAttributes()));

        return it.hasNext();
    }

    @Override
    public OsmWay next() {
        Map.Entry<Long, OsmWay> next = it.next();

        while (!filter.apply(next.getValue().getAttributes())) {
            next = it.next();
        }
        return next.getValue();
    }
}

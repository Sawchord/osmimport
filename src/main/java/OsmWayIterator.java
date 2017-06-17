import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class OsmWayIterator implements Iterator<OsmWay> {

    private Iterator<Map.Entry<Long, OsmWay>> it;
    private Function<OsmWay, Boolean> filter;

    private Map.Entry<Long, OsmWay> current;
    private boolean current_active;

    public OsmWayIterator(Map<Long, OsmWay> map) {
        this.it = map.entrySet().iterator();
        this.filter = x -> true;

        this.current_active = false;
    }

    public void SetFilter(Function<OsmWay, Boolean> filter) {
        this.filter = filter;
    }

    @Override
    public void remove() {
        it.remove();
    }

    @Override
    public boolean hasNext() {

        if (!it.hasNext()) return false;


        while (it.hasNext()) {

            current = it.next();
            current_active = true;

            if  (filter.apply(current.getValue())) {
                break;
            }
        }
        return it.hasNext();
    }

    @Override
    public OsmWay next() {

        if (current_active) {
            current_active = false;
            return current.getValue();
        }

        Map.Entry<Long, OsmWay> next = it.next();

        while (!filter.apply(next.getValue())) {
            next = it.next();
        }
        return next.getValue();
    }
}

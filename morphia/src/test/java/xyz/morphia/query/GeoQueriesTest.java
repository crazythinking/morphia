package xyz.morphia.query;

import org.junit.Before;
import org.junit.Test;
import xyz.morphia.Datastore;
import xyz.morphia.TestBase;
import xyz.morphia.geo.City;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static xyz.morphia.geo.GeoJson.point;
import static xyz.morphia.geo.PointBuilder.pointBuilder;

public class GeoQueriesTest extends TestBase {
    @Override
    @Before
    public void setUp() {
        // this whole test class is designed for "modern" geo queries
        checkMinServerVersion(2.4);
        super.setUp();
    }

    @Test
    public void shouldFindCitiesCloseToAGivenPointWithinARadiusOfMeters() {
        // given
        double latitude = 51.5286416;
        double longitude = -0.1015987;
        Datastore datastore = getDs();
        City london = new City("London", point(latitude, longitude));
        datastore.save(london);
        City manchester = new City("Manchester", point(53.4722454, -2.2235922));
        datastore.save(manchester);
        City sevilla = new City("Sevilla", point(37.3753708, -5.9550582));
        datastore.save(sevilla);

        getDs().ensureIndexes();

        // when
        List<City> citiesOrderedByDistanceFromLondon = toList(datastore.find(City.class)
                                                                       .field("location")
                                                                       .near(pointBuilder().latitude(latitude)
                                                                                           .longitude(longitude).build(), 200000)
                                                                       .find());

        // then
        assertThat(citiesOrderedByDistanceFromLondon.size(), is(1));
        assertThat(citiesOrderedByDistanceFromLondon.get(0), is(london));
    }
}

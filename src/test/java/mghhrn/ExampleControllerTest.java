package mghhrn;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import mghhrn.data.Person;
import mghhrn.data.PersonRepository;
import mghhrn.weather.WeatherClient;
import mghhrn.weather.WeatherResponse;

public class ExampleControllerTest {

    private ExampleController subject;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private WeatherClient weatherClient;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new ExampleController(personRepository, weatherClient);
    }

    @Test
    public void shouldReturnHelloWorld() throws Exception {
        assertThat(subject.hello(), is("Hello World!"));
    }

    @Test
    public void shouldReturnFullNameOfAPerson() throws Exception {
        Person peter = new Person("Peter", "Pan");
        given(personRepository.findByLastName("Pan")).willReturn(Optional.of(peter));

        String greeting = subject.hello("Pan");

        assertThat(greeting, is("Hello Peter Pan!"));
    }

    @Test
    public void shouldTellIfPersonIsUnknown() throws Exception {
        given(personRepository.findByLastName(anyString())).willReturn(Optional.empty());

        String greeting = subject.hello("Pan");

        assertThat(greeting, is("Who is this 'Pan' you're talking about?"));
    }

    @Test
    public void shouldReturnWeatherClientResult() throws Exception {
        WeatherResponse weatherResponse = new WeatherResponse("Hamburg, 8°C raining");
        given(weatherClient.fetchWeather()).willReturn(Optional.of(weatherResponse));

        String weather = subject.weather();

        assertThat(weather, is("Hamburg, 8°C raining"));
    }

    @Test
    public void shouldReturnErrorMessageIfWeatherClientIsUnavailable() throws Exception {
        given(weatherClient.fetchWeather()).willReturn(Optional.empty());

        String weather = subject.weather();

        assertThat(weather, is("Sorry, I couldn't fetch the weather for you :("));
    }
}
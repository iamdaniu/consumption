package de.daniu.home.ablesung.service;

import de.daniu.home.ablesung.Ablesung;
import de.daniu.home.ablesung.AblesungsArt;
import de.daniu.home.ablesung.db.AblesungEntity;
import de.daniu.home.ablesung.db.AblesungRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryAblesungServiceTest {
    @InjectMocks
    private RepositoryAblesungService ablesungService = new RepositoryAblesungService(AblesungsArt.WATER);

    @Mock
    private AblesungRepository repository;

    @Test
    public void addAblesung() {
        Ablesung ablesung = mock(Ablesung.class);
        LocalDate datum = LocalDate.of(2018, 9, 1);
        when(ablesung.getDatum()).thenReturn(datum);
        BigDecimal value = BigDecimal.TEN;
        when(ablesung.getWert()).thenReturn(value);
        ArgumentCaptor<AblesungEntity> savedCaptor = ArgumentCaptor.forClass(AblesungEntity.class);

        ablesungService.addAblesung(ablesung);

        verify(repository).save(savedCaptor.capture());
        AblesungEntity captured = savedCaptor.getValue();
        assertThat(captured.getDatum()).isEqualTo(datum);
        assertThat(captured.getWert()).isEqualByComparingTo(BigDecimal.TEN);
    }

    @Test
    public void getAblesungen() {
        List<AblesungEntity> fromRepo = Arrays.asList(mock(AblesungEntity.class), mock(AblesungEntity.class), mock(AblesungEntity.class));
        LocalDate von = LocalDate.of(2018, 9, 1);
        LocalDate bis = LocalDate.of(2018, 9, 30);
        when(repository.streamByArtAndDatumBetween(AblesungsArt.WATER, von, bis)).thenReturn(fromRepo.stream());

        List<Ablesung> ablesungen = ablesungService.getAblesungen(von, bis).collect(Collectors.toList());

        verify(repository).streamByArtAndDatumBetween(AblesungsArt.WATER, von, bis);
        assertThat(ablesungen).isEqualTo(fromRepo);
    }
}
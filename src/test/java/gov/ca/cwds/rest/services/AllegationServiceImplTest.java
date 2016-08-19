package gov.ca.cwds.rest.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import gov.ca.cwds.rest.api.persistence.legacy.Allegation;

public class AllegationServiceImplTest {
	private static AllegationService allegationService;

	private CrudsService<Allegation> crudsService;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		crudsService = mock(CrudsService.class);
		allegationService = new AllegationServiceImpl(crudsService);
	}

	@Test
	public void findDelegatesToCrudsService() {
		allegationService.find("1");
		verify(crudsService, times(1)).find("1");

	}

	@Test
	public void deleteDelegatesToCrudsService() {
		allegationService.delete("1");
		verify(crudsService, times(1)).delete("1");
	}

	@Test
	public void createDelegatesToCrudsService() {
		Allegation toCreate = new Allegation();
		allegationService.create(toCreate);
		verify(crudsService, times(1)).create(toCreate);
	}

	@Test
	public void updateDelegatesToCrudsService() {
		Allegation toUpdate = new Allegation();
		allegationService.update(toUpdate);
		verify(crudsService, times(1)).update(toUpdate);
	}
}

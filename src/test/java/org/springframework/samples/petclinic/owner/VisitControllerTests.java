package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ContextConfiguration(classes=ApplicationTestConfig.class)

//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration

@WebMvcTest(VisitController.class)
public class VisitControllerTests {

	private static final int TEST_PET_ID = 20;
	private static final int TEST_OWNER_ID = 20;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private VisitRepository visitRepository;

	@MockBean
	private PetRepository petRepository;


	private List<Pet> pets;
	private List<Visit> visits;

	@BeforeEach
	void setup() {
		this.pets = new ArrayList<>();
		this.visits = new ArrayList<>();

		PetType petType = new PetType();
		petType.setName("german");
		petType.setId(20);

		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");

		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(1998, 1, 1));
		pet.setType(petType);
		pet.setId(TEST_PET_ID);
		pet.setName("lucky");
		pet.setOwner(owner);

		this.pets.add(pet);


		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2000, 1, 1));
		visit.setDescription("ordinary checkup");
		visit.setPetId(pet.getId());
		visit.setId(20);

		this.visits.add(visit);

	}

	@Test
	public void initTest() {
		Assert.assertNotNull(mvc);
	}



	@Test
	public void initNewVisitFormTest() throws Exception {
		given(this.petRepository.findById(TEST_PET_ID)).willReturn(this.pets.get(0));
		given(this.visitRepository.findByPetId(TEST_PET_ID)).willReturn(this.visits);

		mvc.perform(get("/owners/20/pets/{petId}/visits/new", TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pet"))
			.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@Test
	public void processNewVisitFormError() throws Exception {
		given(this.petRepository.findById(TEST_PET_ID)).willReturn(this.pets.get(0));
		given(this.visitRepository.findByPetId(TEST_PET_ID)).willReturn(this.visits);

		mvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_PET_ID, TEST_OWNER_ID)
			.param("pet_id", String.valueOf(TEST_PET_ID)).param("date", "2002-01-01"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pet"))
			.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}


	@Test
	public void processNewVisitFormSuccess() throws Exception {
		given(this.petRepository.findById(TEST_PET_ID)).willReturn(this.pets.get(0));
		given(this.visitRepository.findByPetId(TEST_PET_ID)).willReturn(this.visits);


		mvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_PET_ID, TEST_OWNER_ID)
			.param("visit_date", "2002-01-01").param("description", "new visit")
			.param("pet_id", String.valueOf(TEST_PET_ID)))
			.andExpect(status().is3xxRedirection());

		verify(this.visitRepository).save(any(Visit.class));
	}

}

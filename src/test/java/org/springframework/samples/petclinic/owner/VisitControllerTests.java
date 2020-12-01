package org.springframework.samples.petclinic.owner;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.swing.text.html.HTML;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ContextConfiguration(classes=ApplicationTestConfig.class)

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class VisitControllerTests {


	@MockBean
	VisitRepository visitRepository;

	@MockBean
	PetRepository petRepository;

	@InjectMocks
	VisitController visitController;

	@Autowired
	MockMvc mockMvc;

	public List<Pet> pets;
	public List<Visit> visits;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.pets = new ArrayList<>();
		this.visits = new ArrayList<>();

		PetType petType = new PetType();
		petType.setName("german");
		petType.setId(20);

		Owner owner = new Owner();
		owner.setId(20);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");

		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(1998, 1, 1));
		pet.setType(petType);
		pet.setId(20);
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
		Assert.assertNotNull(this.visitController);
//		Assert.assertNotNull(this.visitController.getVisits());
	}


	@Test
	public void initNewVisitFormTest() throws Exception {
		given(this.petRepository.findById(20)).willReturn(this.pets.get(0));
		given(this.visitRepository.findByPetId(20)).willReturn(this.visits);
		MvcResult result = mockMvc.perform(
			get("/owners/20/pets/20/visits/new")
			.accept(MediaType.ALL)
		)
			.andExpect(status().isOk())
			.andReturn();

		String content = result.getResponse().getContentAsString();
		Assert.assertNotNull(content);
	}

}

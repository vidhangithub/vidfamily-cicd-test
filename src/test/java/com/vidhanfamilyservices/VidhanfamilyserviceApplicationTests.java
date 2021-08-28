package com.vidhanfamilyservices;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.vidhanfamilyservices.model.VidhanFamily;
import com.vidhanfamilyservices.repository.VidhanFamilyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class VidhanFamilyServiceApplicationTests {
    @Autowired
    private VidhanFamilyRepository vidhanFamilyRepository;
    @Autowired
    MockMvc mockMvc;

    ObjectMapper om = JsonMapper.builder()
            .findAndAddModules()
            .build();
    Map<String, VidhanFamily> testData;

    @BeforeEach
    public void init() {
        vidhanFamilyRepository.deleteAll();
         testData = getTestData();
    }
    @Test
    public void testFamilyCreationWithValidData() throws Exception {
        VidhanFamily expectedRecord = testData.get("familyMember1");
        VidhanFamily actualRecord = om.readValue(mockMvc.perform(post("/vidhanapi/v1.0/familymember")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.familyMemberId", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), VidhanFamily.class);
        Assertions.assertTrue(new ReflectionEquals(expectedRecord, "familyMemberId").matches(actualRecord));
        assertEquals(true, vidhanFamilyRepository.findById(actualRecord.getFamilyMemberId()).isPresent());

        expectedRecord = testData.get("familyMember2");
        actualRecord =  om.readValue(mockMvc.perform(post("/vidhanapi/v1.0/familymember")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.familyMemberId", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), VidhanFamily.class);

        Assertions.assertTrue(new ReflectionEquals(expectedRecord, "familyMemberId").matches(actualRecord));
        assertEquals(true, vidhanFamilyRepository.findById(actualRecord.getFamilyMemberId()).isPresent());

    }

    private Map<String, VidhanFamily> getTestData() {
        Map<String, VidhanFamily> data = new HashMap<>();

        VidhanFamily familyMember1 = new VidhanFamily(Long.valueOf(1),"vidhan","R12345", LocalDate.now());
        data.put("familyMember1", familyMember1);
        VidhanFamily familyMember2 = new VidhanFamily(Long.valueOf(2),"Rita","R123454", LocalDate.now());
        data.put("familyMember2", familyMember2);
        VidhanFamily familyMember3 = new VidhanFamily(Long.valueOf(3),"Lipsika","R123495", LocalDate.now());
        data.put("familyMember3", familyMember3);
        VidhanFamily familyMember4 = new VidhanFamily(Long.valueOf(4),"Kinjal","R42345", LocalDate.now());
        data.put("familyMember4", familyMember4);
        VidhanFamily familyMember5 = new VidhanFamily(Long.valueOf(4),"Raju","R1234885", LocalDate.now());
        data.put("familyMember5", familyMember5);


        return data;
    }

}

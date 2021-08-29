package com.vidhanfamilyservices;


import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


    @Test
    public void testGetAllMembers() throws Exception {
        Map<String, VidhanFamily> testData = getTestData().entrySet().stream().filter(kv -> "familyMember1,familyMember2".contains(kv.getKey())).collect(Collectors.toMap(kv -> kv.getKey(), kv -> kv.getValue()));

        List<VidhanFamily> expectedRecords = new ArrayList<>();
        for (Map.Entry<String, VidhanFamily> kv : testData.entrySet()) {
            expectedRecords.add(om.readValue(mockMvc.perform(post("/vidhanapi/v1.0/familymember")
                    .contentType("application/json")
                    .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), VidhanFamily.class));
        }
        Collections.sort(expectedRecords, Comparator.comparing(VidhanFamily::getFamilyMemberId));

        List<VidhanFamily> actualRecords = om.readValue(mockMvc.perform(get("/vidhanapi/v1.0/familymembers"))
                .andDo(print())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(expectedRecords.size())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<VidhanFamily>>() {
        });

        for (int i = 0; i < expectedRecords.size(); i++) {
            Assertions.assertTrue(new ReflectionEquals(expectedRecords.get(i)).matches(actualRecords.get(i)));
        }
    }

    @Test
    public void testGetMemberRecordWithId() throws Exception {
        VidhanFamily expectedRecord = getTestData().get("familyMember1");

        expectedRecord = om.readValue(mockMvc.perform(post("/vidhanapi/v1.0/familymember")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), VidhanFamily.class);

        VidhanFamily actualRecord = om.readValue(mockMvc.perform(get("/vidhanapi/v1.0/familymember/" + expectedRecord.getFamilyMemberId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), VidhanFamily.class);

        Assertions.assertTrue(new ReflectionEquals(expectedRecord).matches(actualRecord));

        //non existing record test
        mockMvc.perform(get("/vidhanapi/v1.0/familymember/" + Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testNotAllowedMethod() throws Exception {
        VidhanFamily expectedRecord = getTestData().get("familyMember1");

        VidhanFamily actualRecord = om.readValue(mockMvc.perform(post("/vidhanapi/v1.0/familymember")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), VidhanFamily.class);

        mockMvc.perform(put("/vidhanapi/v1.0/familymember/" + actualRecord.getFamilyMemberId()))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(patch("/vidhanapi/v1.0/familymember/" +  actualRecord.getFamilyMemberId()))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(delete("/vidhanapi/v1.0/familymember/" +  actualRecord.getFamilyMemberId()))
                .andExpect(status().isMethodNotAllowed());
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

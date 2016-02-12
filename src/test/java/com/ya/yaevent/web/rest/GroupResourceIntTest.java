package com.ya.yaevent.web.rest;

import com.ya.yaevent.Application;
import com.ya.yaevent.domain.Group;
import com.ya.yaevent.repository.GroupRepository;
import com.ya.yaevent.service.GroupService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the GroupResource REST controller.
 *
 * @see GroupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GroupResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);

    private static final ZonedDateTime DEFAULT_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFICATION_DATE_STR = dateTimeFormatter.format(DEFAULT_MODIFICATION_DATE);

    private static final Double DEFAULT_VERSION = 1D;
    private static final Double UPDATED_VERSION = 2D;
    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";

    private static final Integer DEFAULT_EVENT_SIZE = 1;
    private static final Integer UPDATED_EVENT_SIZE = 2;
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private GroupService groupService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGroupMockMvc;

    private Group group;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GroupResource groupResource = new GroupResource();
        ReflectionTestUtils.setField(groupResource, "groupService", groupService);
        this.restGroupMockMvc = MockMvcBuilders.standaloneSetup(groupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        groupRepository.deleteAll();
        group = new Group();
        group.setCreationDate(DEFAULT_CREATION_DATE);
        group.setModificationDate(DEFAULT_MODIFICATION_DATE);
        group.setVersion(DEFAULT_VERSION);
        group.setStatus(DEFAULT_STATUS);
        group.setEventSize(DEFAULT_EVENT_SIZE);
        group.setType(DEFAULT_TYPE);
        group.setName(DEFAULT_NAME);
        group.setDescription(DEFAULT_DESCRIPTION);
        group.setLocation(DEFAULT_LOCATION);
    }

    @Test
    public void createGroup() throws Exception {
        int databaseSizeBeforeCreate = groupRepository.findAll().size();

        // Create the Group

        restGroupMockMvc.perform(post("/api/groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(group)))
                .andExpect(status().isCreated());

        // Validate the Group in the database
        List<Group> groups = groupRepository.findAll();
        assertThat(groups).hasSize(databaseSizeBeforeCreate + 1);
        Group testGroup = groups.get(groups.size() - 1);
        assertThat(testGroup.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testGroup.getModificationDate()).isEqualTo(DEFAULT_MODIFICATION_DATE);
        assertThat(testGroup.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testGroup.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testGroup.getEventSize()).isEqualTo(DEFAULT_EVENT_SIZE);
        assertThat(testGroup.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGroup.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    public void getAllGroups() throws Exception {
        // Initialize the database
        groupRepository.save(group);

        // Get all the groups
        restGroupMockMvc.perform(get("/api/groups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(group.getId())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.doubleValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].eventSize").value(hasItem(DEFAULT_EVENT_SIZE)))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }

    @Test
    public void getGroup() throws Exception {
        // Initialize the database
        groupRepository.save(group);

        // Get the group
        restGroupMockMvc.perform(get("/api/groups/{id}", group.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(group.getId()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.modificationDate").value(DEFAULT_MODIFICATION_DATE_STR))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.eventSize").value(DEFAULT_EVENT_SIZE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    public void getNonExistingGroup() throws Exception {
        // Get the group
        restGroupMockMvc.perform(get("/api/groups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateGroup() throws Exception {
        // Initialize the database
        groupRepository.save(group);

		int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Update the group
        group.setCreationDate(UPDATED_CREATION_DATE);
        group.setModificationDate(UPDATED_MODIFICATION_DATE);
        group.setVersion(UPDATED_VERSION);
        group.setStatus(UPDATED_STATUS);
        group.setEventSize(UPDATED_EVENT_SIZE);
        group.setType(UPDATED_TYPE);
        group.setName(UPDATED_NAME);
        group.setDescription(UPDATED_DESCRIPTION);
        group.setLocation(UPDATED_LOCATION);

        restGroupMockMvc.perform(put("/api/groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(group)))
                .andExpect(status().isOk());

        // Validate the Group in the database
        List<Group> groups = groupRepository.findAll();
        assertThat(groups).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groups.get(groups.size() - 1);
        assertThat(testGroup.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testGroup.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testGroup.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testGroup.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testGroup.getEventSize()).isEqualTo(UPDATED_EVENT_SIZE);
        assertThat(testGroup.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGroup.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    public void deleteGroup() throws Exception {
        // Initialize the database
        groupRepository.save(group);

		int databaseSizeBeforeDelete = groupRepository.findAll().size();

        // Get the group
        restGroupMockMvc.perform(delete("/api/groups/{id}", group.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Group> groups = groupRepository.findAll();
        assertThat(groups).hasSize(databaseSizeBeforeDelete - 1);
    }
}

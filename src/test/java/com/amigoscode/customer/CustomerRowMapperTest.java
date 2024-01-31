package com.amigoscode.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)// This is very important in order for @Mock annotation to work properly
class CustomerRowMapperTest {

    @Mock
    private ResultSet mockedResultSet;

    @Test
    void testMapRow() throws SQLException {
        CustomerRowMapper rowMapperToTest = new CustomerRowMapper();
        when(mockedResultSet.getLong("id")).thenReturn(1L);
        when(mockedResultSet.getString("name")).thenReturn("Lio Messi");
        when(mockedResultSet.getString("email")).thenReturn("Lio_Messi@g.com");
        when(mockedResultSet.getInt("age")).thenReturn(36);

        Customer mappedCustomer = rowMapperToTest.mapRow(mockedResultSet, 1);
        assertThat(mappedCustomer.getId()).isEqualTo(1L);
        assertThat(mappedCustomer.getName()).isEqualTo("Lio Messi");
        assertThat(mappedCustomer.getEmail()).isEqualTo("Lio_Messi@g.com");
        assertThat(mappedCustomer.getAge()).isEqualTo(36);
    }
}
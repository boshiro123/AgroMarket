package com.AgroMarket.mapper;

import com.AgroMarket.dto.UserRegistrationDto;
import com.AgroMarket.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "roles", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "subscribeToNewsletter", constant = "false")
  User toUser(UserRegistrationDto registrationDto);
}
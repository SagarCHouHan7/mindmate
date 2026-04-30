package com.MindMate.dto.publicDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TipDto {
    String tip;
    LocalDate date;
}

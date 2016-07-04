function focusForm(index) {
			if (index == 0) {
				$('#formUnit\\:tabUnitLevel\\:textSearchMSSSI').focus();
			} else {
				if (index == 1) {
					$('#formUnit\\:tabUnitLevel\\:textSearchEstatal').focus();
				} else {
					if (index == 2) {
						$('#formUnit\\:tabUnitLevel\\:textSearchAutonomico')
								.focus();
					} else {
						if (index == 3) {
							$('#formUnit\\:tabUnitLevel\\:textSearchLocal')
									.focus();
						} else {
							if (index == 4) {
								$('#formUnit\\:tabUnitLevel\\:textSearchLab')
										.focus();
							} else {
								if (index == 5) {
									$(
											'#formUnit\\:tabUnitLevel\\:textSearchOtros')
											.focus();
								}
							}
						}
					}
				}
			}
			return true;
		}

function focusFormTram(index) {
	if (index == 0) {
		$('#formUnit\\:tabUnitLevel\\:textSearchEstatal').focus();
	} else {
		if (index == 1) {
			$('#formUnit\\:tabUnitLevel\\:textSearchAutonomico')
			.focus();
		} else {
			if (index == 2) {
				$('#formUnit\\:tabUnitLevel\\:textSearchLocal')
				.focus();
			} 
		}
	}
	return true;
}


function clicForm() {
	index = tabUnitLevelVar.getActiveIndex();
	if (index == 0) {
		document
		.getElementById("formUnit:tabUnitLevel:SearchMSSSIButton").click();
	} else {
		if (index == 1) {
			document
			.getElementById(
					"formUnit:tabUnitLevel:SearchEstatalButton")
			.click();
		} else {
			if (index == 2) {
				document
				.getElementById(
						"formUnit:tabUnitLevel:SearchAutonomicoButton")
				.click();
			} else {
				if (index == 3) {
					document
					.getElementById(
							"formUnit:tabUnitLevel:SearchLocalButton")
					.click();
				} else {
					if (index == 4) {
						document
						.getElementById(
								"formUnit:tabUnitLevel:SearchLabButton")
						.click();
					} else {
						if (index == 5) {
							document
							.getElementById(
									"formUnit:tabUnitLevel:SearchOtrosButton")
							.click();
						}
					}
				}
			}
		}
	}
	return true;
}

function clicFormTram() {
	index = tabUnitLevelVar.getActiveIndex();
	if (index == 0) {
		document
		.getElementById(
				"formUnit:tabUnitLevel:SearchEstatalButton")
		.click();
	} else {
		if (index == 1) {
			document
			.getElementById(
					"formUnit:tabUnitLevel:SearchAutonomicoButton")
			.click();
		} else {
			if (index == 2) {
				document
				.getElementById(
						"formUnit:tabUnitLevel:SearchLocalButton")
				.click();
			} 
		}
	}
	return true;
}

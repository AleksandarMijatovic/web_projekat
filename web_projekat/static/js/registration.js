function initialIsCapital( word ){
  return word.charAt(0) !== word.charAt(0).toLowerCase();
}


Vue.component("registration", {
	data: function () {
	    return {
	       gender:''
	    }
},
	template: ` 
<div>
<form v-on:submit.prevent="checkFormValid" method="post">
	<table class="table">
		<tr>
			<td>Korisnicko ime:</td>
			<td><input class="input" placeholder="Unesite korisnicko ime" type="text" /></td>
				
		</tr>
		<tr>
			<td>Ime:</td>
			<td><input class="input" placeholder="Unesite ime" type="text"/></td>
				
		</tr>
		<tr>
			<td>Prezime:</td>
			<td><input class="input" placeholder="Unesite prezime" type="text"/></td>
				
		</tr>
		<tr>
			<td>Pol:</td>
			<td>
  				<div class="pol"><input type="radio" v-model="gender" value="male"> Muski<br></div>
  				<div class="pol"><input type="radio" v-model="gender" value="female"> Zenski<br></div>
			</td>
				
		</tr>
		<tr>
			<td>Lozinka:</td>
			<td><input class="input" placeholder="Unesite lozinku" type="password"/></td>
					
		</tr>
		<tr>
			<td>Ponovo unesite lozinku:</td>
			<td><input class="input" placeholder="Ponovo unesite lozinku" type="password"/></td>	
				
		</tr>
		<tr>
			<td colspan="3" align="center"><input type="submit" value="Registruj se"/></td>
		</tr>
	</table>
</form>
</div>
`
});


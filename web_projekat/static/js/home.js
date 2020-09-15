Vue.component("home-page", {
	data: function () {
	    return {
	       
	        visibleSearchBar: false,
	        dateFrom:'',
	        dateTo:'',
	        userType:'NoUser',
	        amenities: null,
	        selectedAmenities: [],
	    	apartments: null
	    }
},
	template: ` 
		<div>
	<table class="searchtable">
		<tr v-bind:hidden="visibleSearchBar">
			<td><button class="button" v-on:click="openSearch">Otvori pretragu</button></td>	
		</tr>
		<tr v-bind:hidden="!visibleSearchBar" >
			<td>LOKACIJA<input class="searchInput" placeholder="Lokacija" type="text" /></td>
			<td>BROJ OSOBA<input class="searchInput" placeholder="Broj osoba" min=0 type="number"  /></td>
			
		</tr>
		<tr v-bind:hidden="!visibleSearchBar"><label>DATUM</label></tr>
		<tr v-bind:hidden="!visibleSearchBar" >	
			<td>Datum od: <vuejs-datepicker v-model="dateFrom"></vuejs-datepicker></td>
			<td>Datum do: <vuejs-datepicker v-model="dateTo"></vuejs-datepicker></td>
		</tr>
		<tr v-bind:hidden="!visibleSearchBar"><label>BROJ SOBA</label></tr>
		<tr v-bind:hidden="!visibleSearchBar">
			<td><input class="searchInput" placeholder="Minimalno soba" min=0 type="number" /></td>
			<td><input class="searchInput" placeholder="Maksimalno soba" min=0 type="number"  /></td>
		</tr>
		<tr v-bind:hidden="!visibleSearchBar"><label>CENA</label></tr>
		<tr v-bind:hidden="!visibleSearchBar">
			<td><input class="searchInput" placeholder="Minimalna cena" min=0 type="number" v-model="minPrice" name="minPrice"/></td>
			<td><input class="searchInput" placeholder="Maksimalna cena" min=0 type="number" v-model="maxPrice" name="maxPrice"/></td
		</tr>
		
		<tr v-bind:hidden="!visibleSearchBar"><label>SADRŽAJ</label></tr>
		<tr v-bind:hidden="!visibleSearchBar" v-for="(amenity, index) in amenities">
			<input type="checkbox" v-bind:value="amenity" v-model="selectedAmenities" :value="amenity"/>
          {{amenity.name}}
		</tr>
          
		
		<tr v-bind:hidden="!visibleSearchBar">
			<td> TIP SMEŠTAJA
				<select class="select" name="apartmentType" v-model="type">
				   <option class="option" value=""></option>
				   <option class="option" value="soba">Soba</option>
				   <option class="option" value="apartman">Apartman</option>
				</select>
			</td>
			<td> SORTIRANJE	
				<select class="select" name="sort" v-model="sortValue">
				   <option class="option" value=""></option>
				   <option class="option" value="rastuca">Cena rastuća</option>
				   <option class="option" value="opadajuca">Cena opadajuća</option>
				</select>
			
			</td>
        </tr>
		<tr v-bind:hidden="!visibleSearchBar">
			<td><button class="button" >Pretraži</button></td>		
			<td><button class="button" v-on:click="ponistipretragu">Poništi</button></td>		
			
		</tr>
		
	</table>
	<div v-on:click="selectApartment(apartment.id)"  v-for="(apartment, index) in apartments" style = "margin-left:auto;margin-right:auto;padding:20px;">
          <table class="apartview" v-bind:hidden="showSearched" >
          		<tr>
          			<td rowspan="4" style="width:50%;height:85%">
                        <img :src="apartment.pictures[0]" alt="Detalji" height="250" width="325" style="border:5px transparent;border-radius: 10px;margin-left:25px;">
          			<td><label v-if="apartment.type === 'room'">Soba</label>
          			<label v-else>Ceo apartman</label></td>
          			</td>
         
          			
          		</tr>
          		<tr>
          			<td><label>Adresa: </label>
          			<label style="margin-left:25px;">{{apartment.location.adress.city}} - {{apartment.location.adress.street}} {{apartment.location.adress.streetNumber}}</label></td>
          		</tr>
          		<tr>
          			<td><label>Broj gostiju: </label>
          			<label style="margin-left:50px;">{{apartment.numberOfGuest}}</label></td>
          		</tr>
          		<tr>
          			<td><label>Cena:</label>
          			<label style="margin-left:50px;">{{apartment.priceForNight}} din po nocenju</label></td>
          		</tr>
          
          </table>
	</div>
</div>	`,
			mounted () {
		axios
	     .get('/amenity')
	     .then(response => (this.amenities = response.data)),
	    axios
	      .get('/apartments')
	      .then(response => (this.apartments = response.data)), 
	    axios
        .get('/users/log/test')
        .then(response => {
        	if(response.data == null)
        		this.userType='USER';
        	else 
        		if(response.data.typeOfUser == "Guest")
        			this.userType='GUEST';
        		else if(response.data.typeOfUser == "Host")
        			this.userType='HOST';
        		else 
        			this.userType = 'ADMIN';
        })
	},
			components : { 
				vuejsDatepicker
			},methods : {
				  openSearch : function(){
					  this.visibleSearchBar=true;
				  },
				  ponistipretragu : function(){
					  this.visibleSearchBar=false;
				  }
			}
			
	});
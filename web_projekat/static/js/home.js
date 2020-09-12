Vue.component("home-page", {
	data: function () {
	    return {
	       
	        visibleSearchBar: false,
	        dateFrom:'',
	        dateTo:''
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
		
		<tr v-bind:hidden="!visibleSearchBar">
			<td> TIP SMESTAJA
				<select class="select" name="apartmentType" v-model="type">
				   <option class="option" value=""></option>
				   <option class="option" value="soba">Soba</option>
				   <option class="option" value="apartman">Apartman</option>
				</select>
			</td>
			<td> SORTIRANJE	
				<select class="select" name="sort" v-model="sortValue">
				   <option class="option" value=""></option>
				   <option class="option" value="rastuca">Cena rastuca</option>
				   <option class="option" value="opadajuca">Cena opadajuca</option>
				</select>
			
			</td>
			</tr>
		<tr v-bind:hidden="!visibleSearchBar"><label>SADRZAJ</label></tr>
		<tr v-bind:hidden="!visibleSearchBar">
			<td><button class="button" >Pretraži</button></td>		
			<td><button class="button" v-on:click="ponistipretragu">Poništi</button></td>		
			
		</tr>
		
	</table>
	
		</div>
	
	
	
			`, components : { 
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
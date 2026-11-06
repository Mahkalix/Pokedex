import {types, filterPokemon} from './catalogue.js';
const $=s=>document.querySelector(s), number=id=>String(id).padStart(3,'0');
const art=id=>`https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png`;
let data=[], type='', onlyFavorites=false, limit=24, favorites=new Set();
try { const saved=JSON.parse(localStorage.getItem('pokedex-favorites')??'[]'); if(Array.isArray(saved)) favorites=new Set(saved.filter(Number.isInteger)); } catch {}
const badges=p=>p.types.map(t=>`<span class="type ${t}">${types[t]??t}</span>`).join('');
function updateCount(){ $('#favorite-count').textContent=favorites.size; }
function favorite(id) {
  favorites.has(id)?favorites.delete(id):favorites.add(id);
  try {localStorage.setItem('pokedex-favorites',JSON.stringify([...favorites]));} catch {$('#notice').textContent='Le navigateur ne permet pas de sauvegarder les favoris sur cet appareil.';}
  updateCount();render();
  document.querySelectorAll(`[data-save="${id}"]`).forEach(b=>{b.setAttribute('aria-pressed',favorites.has(id));b.textContent=favorites.has(id)?'♥':'♡';});
}
function render() {
  const results=filterPokemon(data,{search:$('#search').value,type,favorites:onlyFavorites?favorites:null,sort:$('#sort').value});
  $('#result-count').textContent=`${results.length} Pokémon ${onlyFavorites?'dans vos favoris':'à découvrir'}`;
  $('#grid').innerHTML=results.slice(0,limit).map(p=>`<article class="card ${p.types[0]}"><div class="card-top"><span>N° ${number(p.id)}</span><button class="save" data-save="${p.id}" aria-label="Favori : ${p.name}" aria-pressed="${favorites.has(p.id)}">${favorites.has(p.id)?'♥':'♡'}</button></div><button class="pokemon" data-open="${p.id}" aria-label="Voir la fiche de ${p.name}"><div class="image-wrap"><img loading="lazy" src="${art(p.id)}" alt="" width="210" height="210"></div><h3>${p.name}</h3><div class="badges">${badges(p)}</div><span class="card-arrow" aria-hidden="true">↗</span></button></article>`).join('');
  $('#empty').hidden=results.length>0 || !data.length;$('#more').hidden=results.length<=limit;
  document.querySelectorAll('img').forEach(img=>img.addEventListener('error',()=>{img.alt='Illustration indisponible';img.classList.add('image-error');},{once:true}));
}
function openDetail(id) {
  const p=data.find(p=>p.id===id); if(!p)return;
  const labels=['PV','Attaque','Défense','Attaque Spé.','Défense Spé.','Vitesse'];
  $('#detail-body').innerHTML=`<div class="detail-art ${p.types[0]}"><span>N° ${number(p.id)}</span><img src="${art(p.id)}" alt="${p.name}" width="270" height="270"></div><div class="detail-content"><p class="eyebrow">${p.genus}</p><h2>${p.name}</h2><div class="badges">${badges(p)}</div><p class="description">${p.description}</p><div class="measurements"><span>TAILLE<strong>${p.height.toLocaleString('fr')} m</strong></span><span>POIDS<strong>${p.weight.toLocaleString('fr')} kg</strong></span><button class="save" data-save="${id}" aria-label="Favori : ${p.name}" aria-pressed="${favorites.has(id)}">${favorites.has(id)?'♥':'♡'}</button></div><h3>Statistiques de base</h3>${p.stats.map((s,i)=>`<div class="stat"><span>${labels[i]}</span><strong>${s.value}</strong><meter min="0" max="255" value="${s.value}" aria-label="${labels[i]}">${s.value}</meter></div>`).join('')}<div class="detail-nav"><button data-open="${id-1}" ${id===1?'disabled':''}>← Précédent</button><button data-open="${id+1}" ${id===151?'disabled':''}>Suivant →</button></div></div>`;
  if(!$('#detail').open)$('#detail').showModal();
}
document.addEventListener('click',e=>{const save=e.target.closest('[data-save]'),open=e.target.closest('[data-open]');if(save)favorite(Number(save.dataset.save));else if(open)openDetail(Number(open.dataset.open));});
$('#close').onclick=()=>$('#detail').close();$('#detail').addEventListener('click',e=>{if(e.target===$('#detail')){const r=e.target.getBoundingClientRect();if(e.clientX<r.left||e.clientX>r.right||e.clientY<r.top||e.clientY>r.bottom)e.target.close();}});
$('#types').innerHTML=`<button data-type="" aria-pressed="true">Tous les types</button>`+Object.entries(types).map(([key,label])=>`<button data-type="${key}" aria-pressed="false"><i class="dot ${key}"></i>${label}</button>`).join('');
$('#types').onclick=e=>{const b=e.target.closest('button');if(!b)return;type=b.dataset.type;limit=24;document.querySelectorAll('[data-type]').forEach(b=>b.setAttribute('aria-pressed',b.dataset.type===type));render();};
for(const selector of ['#search','#sort'])$(selector).addEventListener('input',()=>{limit=24;render();});
function view(fav){onlyFavorites=fav;limit=24;$('#all').setAttribute('aria-pressed',!fav);$('#favorites').setAttribute('aria-pressed',fav);$('#catalogue-title').innerHTML=fav?'Mes favoris <span>Kanto</span>':'Le Pokédex <span>Kanto</span>';render();if(fav)$('#catalogue').scrollIntoView({behavior:'smooth'});}
$('#all').onclick=()=>view(false);$('#favorites').onclick=()=>view(true);
$('#reset').onclick=()=>{$('#search').value='';$('#sort').value='id';$('#types button').click();};
$('#more').onclick=()=>{limit+=24;render();};
document.addEventListener('keydown',e=>{if(e.key==='/'&&!$('#detail').open&&!['INPUT','TEXTAREA','SELECT'].includes(document.activeElement.tagName)){e.preventDefault();$('#search').focus();}});
async function load(){ $('#error').hidden=true;$('#grid').setAttribute('aria-busy','true');try{const r=await fetch('/pokemon.json');if(!r.ok)throw new Error(r.status);data=await r.json();render();}catch{$('#error').hidden=false;$('#result-count').textContent='Chargement interrompu';}finally{$('#grid').setAttribute('aria-busy','false');}}
$('#retry').onclick=load;updateCount();load();

export const types = {grass:'Plante',poison:'Poison',fire:'Feu',flying:'Vol',water:'Eau',bug:'Insecte',normal:'Normal',electric:'Électrik',ground:'Sol',fairy:'Fée',fighting:'Combat',psychic:'Psy',rock:'Roche',steel:'Acier',ice:'Glace',ghost:'Spectre',dragon:'Dragon'};
export const normalize = value => value.normalize('NFD').replace(/[\u0300-\u036f]/g,'').toLowerCase().trim();
export function filterPokemon(data, {search='',type='',favorites=null,sort='id'}={}) {
  const q=normalize(search).replace(/^#/, '');
  return data.filter(p=>(!q || normalize(p.name).includes(q) || p.english.includes(q) || String(p.id)===q || String(p.id).padStart(3,'0')===q) && (!type || p.types.includes(type)) && (!favorites || favorites.has(p.id)))
    .sort((a,b)=>sort==='name'?a.name.localeCompare(b.name,'fr'):sort==='reverse'?b.id-a.id:a.id-b.id);
}

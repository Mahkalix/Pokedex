import { writeFile } from 'node:fs/promises';
const pokemon = [];
async function get(path) {
  const response = await fetch(`https://pokeapi.co/api/v2/${path}`, { signal: AbortSignal.timeout(30000) });
  if (!response.ok) throw new Error(`${path}: ${response.status}`);
  return response.json();
}
for (let start = 1; start <= 151; start += 8) {
  await Promise.all(Array.from({ length: Math.min(8, 152 - start) }, async (_, i) => {
    const id = start + i;
    const [p, s] = await Promise.all([get(`pokemon/${id}`), get(`pokemon-species/${id}`)]);
    pokemon.push({ id, name: s.names.find(n => n.language.name === 'fr')?.name ?? p.name,
      english: p.name, types: p.types.map(t => t.type.name), height: p.height / 10, weight: p.weight / 10,
      description: s.flavor_text_entries.find(t => t.language.name === 'fr')?.flavor_text.replace(/[\n\f]/g, ' ') ?? '',
      genus: s.genera.find(g => g.language.name === 'fr')?.genus ?? '',
      stats: p.stats.map(s => ({ name: s.stat.name, value: s.base_stat })) });
  }));
  console.log(`${Math.min(start + 7, 151)}/151`);
}
await writeFile(new URL('../public/pokemon.json', import.meta.url), JSON.stringify(pokemon.sort((a,b) => a.id-b.id)));

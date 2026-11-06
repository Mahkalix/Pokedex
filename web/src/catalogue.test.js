import {test} from 'node:test';
import assert from 'node:assert/strict';
import {filterPokemon} from './catalogue.js';
import {readFile} from 'node:fs/promises';
const data=JSON.parse(await readFile(new URL('../public/pokemon.json',import.meta.url)));
test('Kanto snapshot contains exactly 151 complete, ordered records',()=>{assert.equal(data.length,151);data.forEach((p,i)=>{assert.equal(p.id,i+1);assert.equal(p.stats.length,6);assert.ok(p.name&&p.description&&p.types.length);});});
test('search handles accents, French, English and padded numbers',()=>{for(const search of ['bulbizarre','bulbasaur','001','#1'])assert.equal(filterPokemon(data,{search})[0].id,1);assert.equal(filterPokemon(data,{search:'evoli'})[0].id,133);});
test('type, favorites and search are combined before sorting',()=>{assert.deepEqual(filterPokemon(data,{type:'grass',favorites:new Set([1,4,2]),sort:'reverse'}).map(p=>p.id),[2,1]);assert.equal(filterPokemon(data,{favorites:new Set()}).length,0);assert.equal(filterPokemon(data,{search:'inconnu'}).length,0);});

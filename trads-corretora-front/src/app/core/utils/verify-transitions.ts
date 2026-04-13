export default function verifyTransitions(value: string) {
    const parts = value.split('_');
    const prefix = parts[0];
    const suffix = parts.slice(1).join('_');
    switch (prefix) {
        case 'P0':
            switch (suffix) {
                case 'NEW': return `[P0] Novo`;
                case 'QUALIFY': return `[P0] Qualificado`;
                case 'PROPOSAL': return `[P0] Proposta`;
                case 'NEGOTIATION': return `[P0] Negociação`;
                case 'WON': return `[P0] Ganho`;
                case 'LOSE': return `[P0] Perda`;
                default: return value;
            }

        case 'P10':
            switch (suffix) {
                case 'NEW': return `[P10] Novo`;
                case 'DISCOVERY': return `[P10] Descoberta`;
                case 'TECH': return `[P10] Reunião Técnica`;
                case 'PROPOSAL': return `[P10] Proposta`;
                case 'LEGAL': return `[P10] Jurídico`;
                case 'WON': return `[P10] Ganho`;
                case 'LOSE': return `[P10] Perda`;
                default: return value;
            }

        case 'P20':
            switch (suffix) {
                case 'CONTACT': return `[P20] Contato`;
                case 'OFFER': return `[P20] Oferta`;
                case 'WON': return `[P20] Ganho`;
                case 'LOSE': return `[P20] Perda`;
                default: return value;
            }

        default:
            return value;
    }
}